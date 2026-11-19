package br.edu.catolicasc.bookswap.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record LivroRequest(
        @NotBlank(message = "O título é obrigatório.")
        @Size(max = 150, message = "O título deve ter no máximo 150 caracteres.")
        String titulo,

        @NotBlank(message = "O autor é obrigatório.")
        @Size(max = 100, message = "O autor deve ter no máximo 100 caracteres.")
        String autor,

        @NotBlank(message = "O estado de conservação é obrigatório.")
        @Pattern(regexp = "NOVO|SEMINOVO|MARCAS_USO",
                message = "O estado de conservação deve ser NOVO, SEMINOVO ou MARCAS_USO.")
        String estadoConservacao,

        @NotNull(message = "O gênero é obrigatório.")
        Long generoId,

        @NotNull(message = "O proprietário é obrigatório.")
        Long proprietarioId
) {
}
