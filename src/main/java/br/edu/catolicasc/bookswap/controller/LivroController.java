package br.edu.catolicasc.bookswap.controller;

import br.edu.catolicasc.bookswap.dto.LivroRequest;
import br.edu.catolicasc.bookswap.dto.LivroResponse;
import br.edu.catolicasc.bookswap.service.LivroService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

/** RF02 — Catalogação de livros e RF06 — Consulta e filtragem. */
@RestController
@RequestMapping("/api/v1/livros")
public class LivroController {

    private final LivroService service;

    public LivroController(LivroService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<LivroResponse> cadastrar(@Valid @RequestBody LivroRequest req) {
        LivroResponse criado = service.cadastrar(req);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(criado.id()).toUri();
        return ResponseEntity.created(uri).body(criado);
    }

    /** Ex.: GET /api/v1/livros?titulo=hobbit&genero=2&cidade=Jaraguá do Sul */
    @GetMapping
    public List<LivroResponse> consultar(@RequestParam(required = false) String titulo,
                                         @RequestParam(required = false) String autor,
                                         @RequestParam(name = "genero", required = false) Long generoId,
                                         @RequestParam(required = false) String cidade,
                                         @RequestParam(name = "proprietario", required = false) Long proprietarioId) {
        return service.consultarDisponiveis(titulo, autor, generoId, cidade, proprietarioId);
    }

    @GetMapping("/{id}")
    public LivroResponse buscar(@PathVariable Long id) {
        return service.buscar(id);
    }

    @PutMapping("/{id}")
    public LivroResponse atualizar(@PathVariable Long id, @Valid @RequestBody LivroRequest req) {
        return service.atualizar(id, req);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> remover(@PathVariable Long id) {
        service.remover(id);
        return ResponseEntity.noContent().build();
    }
}
