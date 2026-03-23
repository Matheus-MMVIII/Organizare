package com.organizare.validation;

import java.util.Map;
import java.util.regex.Pattern;

import com.organizare.exception.ValidationException;

public final class RequestValidator {
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");
    private static final Pattern PHONE_PATTERN = Pattern.compile("^[0-9+()\\-\\s]{8,20}$");

    private RequestValidator() {
    }

    public static String requireText(Map<String, String> payload, String field, int minLength, int maxLength) {
        String value = payload.get(field);
        if (value == null) {
            throw new ValidationException("Campo obrigatorio: " + field + ".");
        }

        String trimmedValue = value.trim();
        if (trimmedValue.length() < minLength || trimmedValue.length() > maxLength) {
            throw new ValidationException("Campo invalido: " + field + ".");
        }
        return trimmedValue;
    }

    public static String requireEmail(Map<String, String> payload, String field) {
        String email = requireText(payload, field, 5, 120);
        if (!EMAIL_PATTERN.matcher(email).matches()) {
            throw new ValidationException("Email invalido.");
        }
        return email.toLowerCase();
    }

    public static String requirePhone(Map<String, String> payload, String field) {
        String phone = requireText(payload, field, 8, 20);
        if (!PHONE_PATTERN.matcher(phone).matches()) {
            throw new ValidationException("Telefone invalido.");
        }
        return phone;
    }

    public static int requireInt(Map<String, String> payload, String field, int minValue, int maxValue) {
        String rawValue = payload.get(field);
        if (rawValue == null) {
            throw new ValidationException("Campo obrigatorio: " + field + ".");
        }

        try {
            int parsedValue = Integer.parseInt(rawValue.trim());
            if (parsedValue < minValue || parsedValue > maxValue) {
                throw new ValidationException("Campo invalido: " + field + ".");
            }
            return parsedValue;
        } catch (NumberFormatException ex) {
            throw new ValidationException("Campo invalido: " + field + ".");
        }
    }

    public static double requireDecimal(Map<String, String> payload, String field, double minValue, double maxValue) {
        String rawValue = payload.get(field);
        if (rawValue == null) {
            throw new ValidationException("Campo obrigatorio: " + field + ".");
        }

        try {
            double parsedValue = Double.parseDouble(rawValue.trim());
            if (parsedValue < minValue || parsedValue > maxValue) {
                throw new ValidationException("Campo invalido: " + field + ".");
            }
            return parsedValue;
        } catch (NumberFormatException ex) {
            throw new ValidationException("Campo invalido: " + field + ".");
        }
    }
}
