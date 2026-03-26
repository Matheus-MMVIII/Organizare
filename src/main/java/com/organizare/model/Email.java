package com.organizare.model;

public class Email {
    private final String email;

    public Email(String email) {
        if (!isValidEmail())
            throw new IllegalArgumentException("Email inválido: " + email);

        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    private boolean isValidEmail() {
        return email != null && email.matches("^[A-Za-z0-9+_.-]+@(.+)$");
    }
}