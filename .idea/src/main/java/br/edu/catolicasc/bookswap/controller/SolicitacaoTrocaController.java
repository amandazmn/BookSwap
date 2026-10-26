package br.edu.catolicasc.bookswap.controller;

import br.edu.catolicasc.bookswap.dto.AtualizarStatusRequest;
import br.edu.catolicasc.bookswap.dto.SolicitacaoTrocaRequest;
import br.edu.catolicasc.bookswap.dto.SolicitacaoTrocaResponse;
import br.edu.catolicasc.bookswap.service.SolicitacaoTrocaService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

/** RF05 — Solicitação e gerenciamento de trocas. */
@RestController
@RequestMapping("/api/v1/trocas")
public class SolicitacaoTrocaController {

    private final SolicitacaoTrocaService service;

    public SolicitacaoTrocaController(SolicitacaoTrocaService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<SolicitacaoTrocaResponse> criar(@Valid @RequestBody SolicitacaoTrocaRequest req) {
        SolicitacaoTrocaResponse criada = service.criar(req);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(criada.id()).toUri();
        return ResponseEntity.created(uri).body(criada);
    }

    /** Ex.: GET /api/v1/trocas?leitor=1 (enviadas e recebidas pelo leitor 1) */
    @GetMapping
    public List<SolicitacaoTrocaResponse> listar(@RequestParam(name = "leitor", required = false) Long leitorId) {
        return service.listar(leitorId);
    }

    @GetMapping("/{id}")
    public SolicitacaoTrocaResponse buscar(@PathVariable Long id) {
        return service.buscar(id);
    }

    /** Corpo: { "status": "ACEITA" | "RECUSADA", "leitorId": 2 } */
    @PatchMapping("/{id}/status")
    public SolicitacaoTrocaResponse atualizarStatus(@PathVariable Long id,
                                                    @Valid @RequestBody AtualizarStatusRequest req) {
        return service.atualizarStatus(id, req);
    }
}
