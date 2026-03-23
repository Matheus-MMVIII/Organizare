package com.organizare.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public final class DatabaseConfig {
    private DatabaseConfig() {
    }

    public static Connection getConnection() throws SQLException {
        String url = requireEnv("DB_URL");
        String user = requireEnv("DB_USER");
        String password = requireEnv("DB_PASSWORD");
        return DriverManager.getConnection(url, user, password);
    }

    private static String requireEnv(String key) {
        String value = System.getenv(key);
        if (value == null || value.isBlank()) {
            throw new IllegalStateException("Variavel de ambiente ausente: " + key);
        }
        return value;
    }
}
