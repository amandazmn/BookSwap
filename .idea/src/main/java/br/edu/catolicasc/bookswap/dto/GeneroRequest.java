package br.edu.catolicasc.bookswap.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record GeneroRequest(
        @NotBlank(message = "O nome do gênero é obrigatório.")
        @Size(max = 50, message = "O nome do gênero deve ter no máximo 50 caracteres.")
        String nome
) {
}
