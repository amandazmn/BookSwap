package br.edu.catolicasc.bookswap.exception;

/** Conflito com dados existentes (ex.: e-mail duplicado) → HTTP 409. */
public class ConflitoException extends RuntimeException {

    public ConflitoException(String mensagem) {
        super(mensagem);
    }
}
