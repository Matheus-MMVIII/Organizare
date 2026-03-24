package com.organizare.exception;

public class ApiException extends RuntimeException {
    private final int statusCode;

    // Cria uma excecao da API contendo o codigo HTTP que deve ser retornado ao cliente.
    public ApiException(int statusCode, String message) {
        super(message);
        this.statusCode = statusCode;
    }

    // Retorna o codigo HTTP associado a esta excecao.
    public int getStatusCode() {
        return statusCode;
    }
}
