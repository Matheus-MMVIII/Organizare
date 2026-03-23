package com.organizare.config;

public final class AppConfig {
    private static final int DEFAULT_PORT = 8080;
    private static final int DEFAULT_BACKLOG = 50;
    private static final int DEFAULT_MAX_REQUEST_BODY_BYTES = 8192;
    private static final String DEFAULT_ALLOWED_ORIGIN = "http://localhost:5173";

    private AppConfig() {
    }

    public static int getPort() {
        return getInt("PORT", DEFAULT_PORT);
    }

    public static int getBacklog() {
        return getInt("SERVER_BACKLOG", DEFAULT_BACKLOG);
    }

    public static int getMaxRequestBodyBytes() {
        return getInt("MAX_REQUEST_BODY_BYTES", DEFAULT_MAX_REQUEST_BODY_BYTES);
    }

    public static String getAllowedOrigin() {
        return getString("ALLOWED_ORIGIN", DEFAULT_ALLOWED_ORIGIN);
    }

    private static int getInt(String key, int fallback) {
        String value = EnvConfig.get(key);
        if (value == null || value.isBlank()) {
            return fallback;
        }

        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException ex) {
            return fallback;
        }
    }

    private static String getString(String key, String fallback) {
        String value = EnvConfig.get(key);
        return value == null || value.isBlank() ? fallback : value;
    }
}
