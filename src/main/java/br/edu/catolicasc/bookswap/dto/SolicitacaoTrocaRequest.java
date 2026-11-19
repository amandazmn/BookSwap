package br.edu.catolicasc.bookswap.dto;

import jakarta.validation.constraints.NotNull;

public record SolicitacaoTrocaRequest(
        @NotNull(message = "O solicitante é obrigatório.")
        Long solicitanteId,

        @NotNull(message = "O livro desejado é obrigatório.")
        Long livroDesejadoId,

        @NotNull(message = "O livro oferecido é obrigatório.")
        Long livroOferecidoId,

        @NotNull(message = "O ponto de encontro é obrigatório.")
        Long pontoEncontroId
) {
}
