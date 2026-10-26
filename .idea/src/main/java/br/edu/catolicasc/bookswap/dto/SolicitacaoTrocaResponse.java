package br.edu.catolicasc.bookswap.dto;

import br.edu.catolicasc.bookswap.model.SolicitacaoTroca;
import br.edu.catolicasc.bookswap.model.StatusTroca;

import java.time.LocalDate;

public record SolicitacaoTrocaResponse(
        Long id,
        LocalDate dataSolicitacao,
        StatusTroca status,
        Long solicitanteId,
        String solicitante,
        Long livroDesejadoId,
        String livroDesejado,
        Long livroOferecidoId,
        String livroOferecido,
        Long pontoEncontroId,
        String pontoEncontro
) {

    public static SolicitacaoTrocaResponse de(SolicitacaoTroca s) {
        return new SolicitacaoTrocaResponse(
                s.getId(),
                s.getDataSolicitacao(),
                s.getStatus(),
                s.getSolicitante().getId(),
                s.getSolicitante().getNome(),
                s.getLivroDesejado().getId(),
                s.getLivroDesejado().getTitulo(),
                s.getLivroOferecido().getId(),
                s.getLivroOferecido().getTitulo(),
                s.getPontoEncontro().getId(),
                s.getPontoEncontro().getNome());
    }
}
