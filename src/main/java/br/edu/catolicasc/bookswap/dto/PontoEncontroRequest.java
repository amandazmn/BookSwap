package br.edu.catolicasc.bookswap.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record PontoEncontroRequest(
        @NotBlank(message = "O nome do local é obrigatório.")
        @Size(max = 100, message = "O nome deve ter no máximo 100 caracteres.")
        String nome,

        @NotBlank(message = "O endereço é obrigatório.")
        @Size(max = 255, message = "O endereço deve ter no máximo 255 caracteres.")
        String endereco
) {
}
