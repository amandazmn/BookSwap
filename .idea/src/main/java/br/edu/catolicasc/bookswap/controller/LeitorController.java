package br.edu.catolicasc.bookswap.controller;

import br.edu.catolicasc.bookswap.dto.LeitorRequest;
import br.edu.catolicasc.bookswap.dto.LeitorResponse;
import br.edu.catolicasc.bookswap.service.LeitorService;
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

/** RF01 — Cadastro e gerenciamento de leitores. */
@RestController
@RequestMapping("/api/v1/leitores")
public class LeitorController {

    private final LeitorService service;

    public LeitorController(LeitorService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<LeitorResponse> cadastrar(@Valid @RequestBody LeitorRequest req) {
        LeitorResponse criado = service.cadastrar(req);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(criado.id()).toUri();
        return ResponseEntity.created(uri).body(criado);
    }

    @GetMapping
    public List<LeitorResponse> listar() {
        return service.listar();
    }

    @GetMapping("/{id}")
    public LeitorResponse buscar(@PathVariable Long id) {
        return service.buscar(id);
    }

    @PutMapping("/{id}")
    public LeitorResponse atualizar(@PathVariable Long id, @Valid @RequestBody LeitorRequest req) {
        return service.atualizar(id, req);
    }
}
