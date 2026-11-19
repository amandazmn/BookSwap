package br.edu.catolicasc.bookswap.controller;

import br.edu.catolicasc.bookswap.dto.GeneroRequest;
import br.edu.catolicasc.bookswap.dto.GeneroResponse;
import br.edu.catolicasc.bookswap.service.GeneroService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

/** RF03 — Gerenciamento de gêneros literários. */
@RestController
@RequestMapping("/api/v1/generos")
public class GeneroController {

    private final GeneroService service;

    public GeneroController(GeneroService service) {
        this.service = service;
    }

    @GetMapping
    public List<GeneroResponse> listar() {
        return service.listar();
    }

    @GetMapping("/{id}")
    public GeneroResponse buscar(@PathVariable Long id) {
        return service.buscar(id);
    }

    /** Operação administrativa (RF03). */
    @PostMapping
    public ResponseEntity<GeneroResponse> cadastrar(@Valid @RequestBody GeneroRequest req) {
        GeneroResponse criado = service.cadastrar(req);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(criado.id()).toUri();
        return ResponseEntity.created(uri).body(criado);
    }

    /** Operação administrativa (RF03). */
    @PutMapping("/{id}")
    public GeneroResponse atualizar(@PathVariable Long id, @Valid @RequestBody GeneroRequest req) {
        return service.atualizar(id, req);
    }
}
