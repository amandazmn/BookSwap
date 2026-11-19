package br.edu.catolicasc.bookswap.dto;

import br.edu.catolicasc.bookswap.model.Genero;

public record GeneroResponse(Long id, String nome) {

    public static GeneroResponse de(Genero genero) {
        return new GeneroResponse(genero.getId(), genero.getNome());
    }
}
