package com.organizare.exception;

public class ValidationException extends ApiException {
    // Cria uma excecao para falhas de validacao semantica dos dados recebidos.
    public ValidationException(String message) {
        super(422, message);
    }
}
