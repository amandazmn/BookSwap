package br.edu.catolicasc.bookswap.exception;

/** Requisição válida em formato, mas que viola uma regra de negócio → HTTP 400. */
public class RegraNegocioException extends RuntimeException {

    public RegraNegocioException(String mensagem) {
        super(mensagem);
    }
}
