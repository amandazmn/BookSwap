package br.edu.catolicasc.bookswap.dto;

import br.edu.catolicasc.bookswap.model.PontoEncontro;

public record PontoEncontroResponse(Long id, String nome, String endereco, Boolean ativo) {

    public static PontoEncontroResponse de(PontoEncontro ponto) {
        return new PontoEncontroResponse(ponto.getId(), ponto.getNome(), ponto.getEndereco(), ponto.getAtivo());
    }
}
