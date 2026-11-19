package br.edu.catolicasc.bookswap.exception;

import java.time.LocalDateTime;
import java.util.List;

/** Formato padronizado das respostas de erro da API (RF07). */
public record ErroResponse(
        LocalDateTime timestamp,
        int status,
        String erro,
        String mensagem,
        String caminho,
        List<String> detalhes
) {
}
