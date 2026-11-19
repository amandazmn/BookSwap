package br.edu.catolicasc.bookswap.dto;

import br.edu.catolicasc.bookswap.model.Leitor;

public record LeitorResponse(Long id, String nome, String email, String cidade) {

    public static LeitorResponse de(Leitor leitor) {
        return new LeitorResponse(leitor.getId(), leitor.getNome(), leitor.getEmail(), leitor.getCidade());
    }
}
