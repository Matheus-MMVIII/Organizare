package com.organizare.exception;

public class ValidationException extends ApiException {
    public ValidationException(String message) {
        super(422, message);
    }
}
