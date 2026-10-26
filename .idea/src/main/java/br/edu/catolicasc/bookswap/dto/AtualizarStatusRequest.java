package br.edu.catolicasc.bookswap.dto;

import br.edu.catolicasc.bookswap.model.StatusTroca;
import jakarta.validation.constraints.NotNull;

/**
 * Dados mínimos para aceitar ou recusar uma solicitação.
 * leitorId identifica quem está tomando a decisão (RN07).
 */
public record AtualizarStatusRequest(
        @NotNull(message = "O novo status é obrigatório (ACEITA ou RECUSADA).")
        StatusTroca status,

        @NotNull(message = "O leitor responsável pela decisão é obrigatório.")
        Long leitorId
) {
}
