package br.edu.catolicasc.bookswap.service;

import br.edu.catolicasc.bookswap.dto.PontoEncontroRequest;
import br.edu.catolicasc.bookswap.dto.PontoEncontroResponse;
import br.edu.catolicasc.bookswap.exception.RecursoNaoEncontradoException;
import br.edu.catolicasc.bookswap.model.PontoEncontro;
import br.edu.catolicasc.bookswap.repository.PontoEncontroRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PontoEncontroService {

    private final PontoEncontroRepository pontoRepository;

    public PontoEncontroService(PontoEncontroRepository pontoRepository) {
        this.pontoRepository = pontoRepository;
    }

    @Transactional
    public PontoEncontroResponse cadastrar(PontoEncontroRequest req) {
        PontoEncontro ponto = new PontoEncontro(req.nome().trim(), req.endereco().trim());
        return PontoEncontroResponse.de(pontoRepository.save(ponto));
    }

    /** Lista apenas os pontos ativos, que podem receber novas solicitações. */
    @Transactional(readOnly = true)
    public List<PontoEncontroResponse> listarAtivos() {
        return pontoRepository.findByAtivoTrue().stream().map(PontoEncontroResponse::de).toList();
    }

    @Transactional(readOnly = true)
    public PontoEncontroResponse buscar(Long id) {
        return PontoEncontroResponse.de(buscarEntidade(id));
    }

    @Transactional
    public PontoEncontroResponse atualizar(Long id, PontoEncontroRequest req) {
        PontoEncontro ponto = buscarEntidade(id);
        ponto.atualizar(req.nome().trim(), req.endereco().trim());
        return PontoEncontroResponse.de(ponto);
    }

    /** Desativa o ponto em vez de excluí-lo, preservando o histórico (RN10). */
    @Transactional
    public void desativar(Long id) {
        buscarEntidade(id).desativar();
    }

    PontoEncontro buscarEntidade(Long id) {
        return pontoRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Ponto de encontro", id));
    }
}
