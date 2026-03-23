package com.organizare.config;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class EnvConfig {
    private static final Map<String, String> FILE_VALUES = loadEnvFile();

    private EnvConfig() {
    }

    public static String get(String key) {
        String envValue = System.getenv(key);
        if (envValue != null && !envValue.isBlank()) {
            return envValue;
        }
        return FILE_VALUES.get(key);
    }

    private static Map<String, String> loadEnvFile() {
        Path envPath = Path.of(".env");
        if (!Files.exists(envPath)) {
            return Map.of();
        }

        Map<String, String> values = new HashMap<>();
        try {
            List<String> lines = Files.readAllLines(envPath, StandardCharsets.UTF_8);
            for (String line : lines) {
                parseLine(values, line);
            }
        } catch (IOException ex) {
            return Map.of();
        }
        return Map.copyOf(values);
    }

    private static void parseLine(Map<String, String> values, String line) {
        String trimmedLine = line.trim();
        if (trimmedLine.isEmpty() || trimmedLine.startsWith("#")) {
            return;
        }

        int separatorIndex = trimmedLine.indexOf('=');
        if (separatorIndex <= 0) {
            return;
        }

        String key = trimmedLine.substring(0, separatorIndex).trim();
        String rawValue = trimmedLine.substring(separatorIndex + 1).trim();
        if (key.isEmpty()) {
            return;
        }

        values.put(key, stripQuotes(rawValue));
    }

    private static String stripQuotes(String value) {
        if (value.length() >= 2) {
            char first = value.charAt(0);
            char last = value.charAt(value.length() - 1);
            if ((first == '"' && last == '"') || (first == '\'' && last == '\'')) {
                return value.substring(1, value.length() - 1);
            }
        }
        return value;
    }
}
