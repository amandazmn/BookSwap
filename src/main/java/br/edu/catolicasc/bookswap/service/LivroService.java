package br.edu.catolicasc.bookswap.service;

import br.edu.catolicasc.bookswap.dto.LivroRequest;
import br.edu.catolicasc.bookswap.dto.LivroResponse;
import br.edu.catolicasc.bookswap.exception.ConflitoException;
import br.edu.catolicasc.bookswap.exception.RecursoNaoEncontradoException;
import br.edu.catolicasc.bookswap.model.Genero;
import br.edu.catolicasc.bookswap.model.Leitor;
import br.edu.catolicasc.bookswap.model.Livro;
import br.edu.catolicasc.bookswap.model.SolicitacaoTroca;
import br.edu.catolicasc.bookswap.model.StatusTroca;
import br.edu.catolicasc.bookswap.repository.LivroRepository;
import br.edu.catolicasc.bookswap.repository.SolicitacaoTrocaRepository;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Subquery;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class LivroService {

    private final LivroRepository livroRepository;
    private final SolicitacaoTrocaRepository solicitacaoRepository;
    private final LeitorService leitorService;
    private final GeneroService generoService;

    public LivroService(LivroRepository livroRepository,
                        SolicitacaoTrocaRepository solicitacaoRepository,
                        LeitorService leitorService,
                        GeneroService generoService) {
        this.livroRepository = livroRepository;
        this.solicitacaoRepository = solicitacaoRepository;
        this.leitorService = leitorService;
        this.generoService = generoService;
    }

    /** Cadastra um livro validando proprietário e gênero (RN02, RN03). */
    @Transactional
    public LivroResponse cadastrar(LivroRequest req) {
        Leitor proprietario = leitorService.buscarEntidade(req.proprietarioId());
        Genero genero = generoService.buscarEntidade(req.generoId());
        Livro livro = new Livro(req.titulo().trim(), req.autor().trim(), req.estadoConservacao(), genero, proprietario);
        return LivroResponse.de(livroRepository.save(livro));
    }

    /**
     * Consulta livros disponíveis para troca (RF06).
     * Um livro é considerado disponível enquanto não fizer parte de uma troca ACEITA.
     * Todos os filtros são opcionais.
     */
    @Transactional(readOnly = true)
    public List<LivroResponse> consultarDisponiveis(String titulo, String autor, Long generoId,
                                                    String cidade, Long proprietarioId) {
        Specification<Livro> spec = (root, query, cb) -> {
            List<Predicate> filtros = new ArrayList<>();
            if (titulo != null && !titulo.isBlank()) {
                filtros.add(cb.like(cb.lower(root.get("titulo")), "%" + titulo.trim().toLowerCase() + "%"));
            }
            if (autor != null && !autor.isBlank()) {
                filtros.add(cb.like(cb.lower(root.get("autor")), "%" + autor.trim().toLowerCase() + "%"));
            }
            if (generoId != null) {
                filtros.add(cb.equal(root.get("genero").get("id"), generoId));
            }
            if (cidade != null && !cidade.isBlank()) {
                filtros.add(cb.equal(cb.lower(root.get("proprietario").get("cidade")), cidade.trim().toLowerCase()));
            }
            if (proprietarioId != null) {
                filtros.add(cb.equal(root.get("proprietario").get("id"), proprietarioId));
            }
            Subquery<Long> trocaAceita = query.subquery(Long.class);
            Root<SolicitacaoTroca> s = trocaAceita.from(SolicitacaoTroca.class);
            trocaAceita.select(s.get("id")).where(
                    cb.equal(s.get("status"), StatusTroca.ACEITA),
                    cb.or(cb.equal(s.get("livroDesejado"), root), cb.equal(s.get("livroOferecido"), root)));
            filtros.add(cb.not(cb.exists(trocaAceita)));
            return cb.and(filtros.toArray(new Predicate[0]));
        };
        return livroRepository.findAll(spec, Sort.by("titulo")).stream().map(LivroResponse::de).toList();
    }

    @Transactional(readOnly = true)
    public LivroResponse buscar(Long id) {
        return LivroResponse.de(buscarEntidade(id));
    }

    /** Atualiza os dados do livro. O proprietário não pode ser alterado. */
    @Transactional
    public LivroResponse atualizar(Long id, LivroRequest req) {
        Livro livro = buscarEntidade(id);
        if (!livro.getProprietario().getId().equals(req.proprietarioId())) {
            throw new ConflitoException("O proprietário de um livro não pode ser alterado.");
        }
        Genero genero = generoService.buscarEntidade(req.generoId());
        livro.atualizar(req.titulo().trim(), req.autor().trim(), req.estadoConservacao(), genero);
        return LivroResponse.de(livro);
    }

    /**
     * Remove o livro. Se ele já participou de solicitações, a remoção é bloqueada
     * para não comprometer o histórico (RN10, RN11).
     */
    @Transactional
    public void remover(Long id) {
        Livro livro = buscarEntidade(id);
        if (solicitacaoRepository.livroPossuiSolicitacoes(id)) {
            throw new ConflitoException("O livro possui solicitações de troca registradas e não pode ser removido.");
        }
        livroRepository.delete(livro);
    }

    Livro buscarEntidade(Long id) {
        return livroRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Livro", id));
    }
}
