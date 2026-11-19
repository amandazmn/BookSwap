package br.edu.catolicasc.bookswap.service;

import br.edu.catolicasc.bookswap.dto.AtualizarStatusRequest;
import br.edu.catolicasc.bookswap.dto.SolicitacaoTrocaRequest;
import br.edu.catolicasc.bookswap.dto.SolicitacaoTrocaResponse;
import br.edu.catolicasc.bookswap.exception.AcessoNegadoException;
import br.edu.catolicasc.bookswap.exception.ConflitoException;
import br.edu.catolicasc.bookswap.exception.RecursoNaoEncontradoException;
import br.edu.catolicasc.bookswap.exception.RegraNegocioException;
import br.edu.catolicasc.bookswap.model.Leitor;
import br.edu.catolicasc.bookswap.model.Livro;
import br.edu.catolicasc.bookswap.model.PontoEncontro;
import br.edu.catolicasc.bookswap.model.SolicitacaoTroca;
import br.edu.catolicasc.bookswap.model.StatusTroca;
import br.edu.catolicasc.bookswap.repository.SolicitacaoTrocaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Service
public class SolicitacaoTrocaService {

    private final SolicitacaoTrocaRepository solicitacaoRepository;
    private final LeitorService leitorService;
    private final LivroService livroService;
    private final PontoEncontroService pontoService;

    public SolicitacaoTrocaService(SolicitacaoTrocaRepository solicitacaoRepository,
                                   LeitorService leitorService,
                                   LivroService livroService,
                                   PontoEncontroService pontoService) {
        this.solicitacaoRepository = solicitacaoRepository;
        this.leitorService = leitorService;
        this.livroService = livroService;
        this.pontoService = pontoService;
    }

    /** Cria uma solicitação aplicando as regras RN04, RN05, RN06, RN08 e RN11. */
    @Transactional
    public SolicitacaoTrocaResponse criar(SolicitacaoTrocaRequest req) {
        Leitor solicitante = leitorService.buscarEntidade(req.solicitanteId());
        Livro desejado = livroService.buscarEntidade(req.livroDesejadoId());
        Livro oferecido = livroService.buscarEntidade(req.livroOferecidoId());
        PontoEncontro ponto = pontoService.buscarEntidade(req.pontoEncontroId());

        if (desejado.getId().equals(oferecido.getId())) {
            throw new RegraNegocioException("O livro desejado e o livro oferecido devem ser diferentes.");
        }
        if (desejado.pertenceA(solicitante)) {
            throw new RegraNegocioException("O leitor não pode solicitar um livro de sua própria propriedade."); // RN04
        }
        if (!oferecido.pertenceA(solicitante)) {
            throw new RegraNegocioException("O livro oferecido deve pertencer ao solicitante."); // RN05
        }
        if (!ponto.isAtivo()) {
            throw new RegraNegocioException("O ponto de encontro selecionado está inativo."); // RN08
        }
        verificarDisponibilidade(desejado);
        verificarDisponibilidade(oferecido);

        SolicitacaoTroca solicitacao = new SolicitacaoTroca(solicitante, desejado, oferecido, ponto); // RN06 no @PrePersist
        return SolicitacaoTrocaResponse.de(solicitacaoRepository.save(solicitacao));
    }

    /**
     * Lista solicitações. Com leitorId, retorna apenas as enviadas pelo leitor
     * e as recebidas por ele (como proprietário do livro desejado).
     */
    @Transactional(readOnly = true)
    public List<SolicitacaoTrocaResponse> listar(Long leitorId) {
        List<SolicitacaoTroca> resultado;
        if (leitorId == null) {
            resultado = solicitacaoRepository.findAll();
        } else {
            leitorService.buscarEntidade(leitorId);
            Set<SolicitacaoTroca> conjunto = new LinkedHashSet<>(solicitacaoRepository.findBySolicitanteId(leitorId));
            conjunto.addAll(solicitacaoRepository.findByLivroDesejadoProprietarioId(leitorId));
            resultado = conjunto.stream().sorted(Comparator.comparing(SolicitacaoTroca::getId)).toList();
        }
        return resultado.stream().map(SolicitacaoTrocaResponse::de).toList();
    }

    @Transactional(readOnly = true)
    public SolicitacaoTrocaResponse buscar(Long id) {
        return SolicitacaoTrocaResponse.de(buscarEntidade(id));
    }

    /** Aceita ou recusa uma solicitação PENDENTE (RN07, RN09). */
    @Transactional
    public SolicitacaoTrocaResponse atualizarStatus(Long id, AtualizarStatusRequest req) {
        SolicitacaoTroca solicitacao = buscarEntidade(id);
        Leitor responsavel = leitorService.buscarEntidade(req.leitorId());

        if (!solicitacao.getLivroDesejado().pertenceA(responsavel)) {
            throw new AcessoNegadoException("Somente o proprietário do livro desejado pode aceitar ou recusar a solicitação.");
        }
        if (!solicitacao.isPendente()) {
            throw new ConflitoException("A solicitação já foi " + solicitacao.getStatus().name().toLowerCase() + " e não pode ser alterada.");
        }
        if (req.status() == StatusTroca.ACEITA) {
            verificarDisponibilidade(solicitacao.getLivroDesejado());
            verificarDisponibilidade(solicitacao.getLivroOferecido());
            solicitacao.aceitar();
        } else if (req.status() == StatusTroca.RECUSADA) {
            solicitacao.recusar();
        } else {
            throw new RegraNegocioException("O novo status deve ser ACEITA ou RECUSADA.");
        }
        return SolicitacaoTrocaResponse.de(solicitacao);
    }

    private void verificarDisponibilidade(Livro livro) {
        if (solicitacaoRepository.livroPossuiSolicitacaoComStatus(livro.getId(), StatusTroca.ACEITA)) {
            throw new ConflitoException("O livro \"" + livro.getTitulo() + "\" já foi trocado e não está mais disponível.");
        }
    }

    private SolicitacaoTroca buscarEntidade(Long id) {
        return solicitacaoRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Solicitação de troca", id));
    }
}
