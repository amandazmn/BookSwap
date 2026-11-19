package br.edu.catolicasc.bookswap.dto;

import br.edu.catolicasc.bookswap.model.Livro;

public record LivroResponse(
        Long id,
        String titulo,
        String autor,
        String estadoConservacao,
        Long generoId,
        String genero,
        Long proprietarioId,
        String proprietario,
        String cidade
) {

    public static LivroResponse de(Livro livro) {
        return new LivroResponse(
                livro.getId(),
                livro.getTitulo(),
                livro.getAutor(),
                livro.getEstadoConservacao(),
                livro.getGenero().getId(),
                livro.getGenero().getNome(),
                livro.getProprietario().getId(),
                livro.getProprietario().getNome(),
                livro.getProprietario().getCidade());
    }
}
