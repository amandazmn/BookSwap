package br.edu.catolicasc.bookswap.service;

import br.edu.catolicasc.bookswap.dto.GeneroRequest;
import br.edu.catolicasc.bookswap.dto.GeneroResponse;
import br.edu.catolicasc.bookswap.exception.ConflitoException;
import br.edu.catolicasc.bookswap.exception.RecursoNaoEncontradoException;
import br.edu.catolicasc.bookswap.model.Genero;
import br.edu.catolicasc.bookswap.repository.GeneroRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class GeneroService {

    private final GeneroRepository generoRepository;

    public GeneroService(GeneroRepository generoRepository) {
        this.generoRepository = generoRepository;
    }

    @Transactional
    public GeneroResponse cadastrar(GeneroRequest req) {
        String nome = req.nome().trim();
        if (generoRepository.existsByNomeIgnoreCase(nome)) {
            throw new ConflitoException("O gênero " + nome + " já está cadastrado.");
        }
        return GeneroResponse.de(generoRepository.save(new Genero(nome)));
    }

    @Transactional(readOnly = true)
    public List<GeneroResponse> listar() {
        return generoRepository.findAll().stream().map(GeneroResponse::de).toList();
    }

    @Transactional(readOnly = true)
    public GeneroResponse buscar(Long id) {
        return GeneroResponse.de(buscarEntidade(id));
    }

    @Transactional
    public GeneroResponse atualizar(Long id, GeneroRequest req) {
        Genero genero = buscarEntidade(id);
        String nome = req.nome().trim();
        if (generoRepository.existsByNomeIgnoreCaseAndIdNot(nome, id)) {
            throw new ConflitoException("O gênero " + nome + " já está cadastrado.");
        }
        genero.atualizar(nome);
        return GeneroResponse.de(genero);
    }

    Genero buscarEntidade(Long id) {
        return generoRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Gênero", id));
    }
}
