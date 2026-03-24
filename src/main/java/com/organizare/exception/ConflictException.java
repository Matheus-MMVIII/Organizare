package com.organizare.exception;

public class ConflictException extends ApiException {
    // Cria uma excecao para representar conflito de estado, como dados duplicados.
    public ConflictException(String message) {
        super(409, message);
    }
}
