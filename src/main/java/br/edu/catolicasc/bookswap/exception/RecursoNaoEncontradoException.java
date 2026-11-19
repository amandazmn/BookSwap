package br.edu.catolicasc.bookswap.exception;

/** Registro inexistente → HTTP 404. */
public class RecursoNaoEncontradoException extends RuntimeException {

    public RecursoNaoEncontradoException(String recurso, Long id) {
        super(recurso + " com id " + id + " não encontrado(a).");
    }
}
