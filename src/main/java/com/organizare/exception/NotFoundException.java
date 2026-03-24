package com.organizare.exception;

public class NotFoundException extends ApiException {
    // Cria uma excecao para indicar que o recurso solicitado nao foi encontrado.
    public NotFoundException(String message) {
        super(404, message);
    }
}
