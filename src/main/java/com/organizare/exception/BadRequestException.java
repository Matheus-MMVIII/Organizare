package com.organizare.exception;

public class BadRequestException extends ApiException {
    // Cria uma excecao para representar requisicoes malformadas ou dados de entrada invalidos.
    public BadRequestException(String message) {
        super(400, message);
    }
}
