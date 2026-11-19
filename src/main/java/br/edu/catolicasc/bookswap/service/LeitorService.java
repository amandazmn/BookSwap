package br.edu.catolicasc.bookswap.service;

import br.edu.catolicasc.bookswap.dto.LeitorRequest;
import br.edu.catolicasc.bookswap.dto.LeitorResponse;
import br.edu.catolicasc.bookswap.exception.ConflitoException;
import br.edu.catolicasc.bookswap.exception.RecursoNaoEncontradoException;
import br.edu.catolicasc.bookswap.model.Leitor;
import br.edu.catolicasc.bookswap.repository.LeitorRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class LeitorService {

    private final LeitorRepository leitorRepository;

    public LeitorService(LeitorRepository leitorRepository) {
        this.leitorRepository = leitorRepository;
    }

    @Transactional
    public LeitorResponse cadastrar(LeitorRequest req) {
        String email = req.email().trim().toLowerCase();
        if (leitorRepository.existsByEmailIgnoreCase(email)) {
            throw new ConflitoException("Já existe um leitor cadastrado com o e-mail " + email + "."); // RN01
        }
        Leitor leitor = new Leitor(req.nome().trim(), email, req.cidade().trim());
        return LeitorResponse.de(leitorRepository.save(leitor));
    }

    @Transactional(readOnly = true)
    public List<LeitorResponse> listar() {
        return leitorRepository.findAll().stream().map(LeitorResponse::de).toList();
    }

    @Transactional(readOnly = true)
    public LeitorResponse buscar(Long id) {
        return LeitorResponse.de(buscarEntidade(id));
    }

    @Transactional
    public LeitorResponse atualizar(Long id, LeitorRequest req) {
        Leitor leitor = buscarEntidade(id);
        String email = req.email().trim().toLowerCase();
        if (leitorRepository.existsByEmailIgnoreCaseAndIdNot(email, id)) {
            throw new ConflitoException("Já existe outro leitor cadastrado com o e-mail " + email + ".");
        }
        leitor.atualizarDados(req.nome().trim(), email, req.cidade().trim());
        return LeitorResponse.de(leitor);
    }

    Leitor buscarEntidade(Long id) {
        return leitorRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Leitor", id));
    }
}
