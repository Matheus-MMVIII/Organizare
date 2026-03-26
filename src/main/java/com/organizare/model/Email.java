package com.organizare.model;

public class Email {
    private final String email;

    public Email(String email) {
        if (!isValidEmail(email))
            throw new IllegalArgumentException("Email inválido: " + email);

        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    private boolean isValidEmail(String email) {
        return email != null && email.matches("^[^&=_'-+,<>]+@(.+)$");
    }
}