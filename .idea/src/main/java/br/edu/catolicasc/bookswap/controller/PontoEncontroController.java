package br.edu.catolicasc.bookswap.controller;

import br.edu.catolicasc.bookswap.dto.PontoEncontroRequest;
import br.edu.catolicasc.bookswap.dto.PontoEncontroResponse;
import br.edu.catolicasc.bookswap.service.PontoEncontroService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
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

/** RF04 — Gerenciamento de pontos de encontro (operações administrativas). */
@RestController
@RequestMapping("/api/v1/pontos-encontro")
public class PontoEncontroController {

    private final PontoEncontroService service;

    public PontoEncontroController(PontoEncontroService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<PontoEncontroResponse> cadastrar(@Valid @RequestBody PontoEncontroRequest req) {
        PontoEncontroResponse criado = service.cadastrar(req);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(criado.id()).toUri();
        return ResponseEntity.created(uri).body(criado);
    }

    @GetMapping
    public List<PontoEncontroResponse> listar() {
        return service.listarAtivos();
    }

    @GetMapping("/{id}")
    public PontoEncontroResponse buscar(@PathVariable Long id) {
        return service.buscar(id);
    }

    @PutMapping("/{id}")
    public PontoEncontroResponse atualizar(@PathVariable Long id, @Valid @RequestBody PontoEncontroRequest req) {
        return service.atualizar(id, req);
    }

    /** Desativa o ponto (exclusão lógica). */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> desativar(@PathVariable Long id) {
        service.desativar(id);
        return ResponseEntity.noContent().build();
    }
}
