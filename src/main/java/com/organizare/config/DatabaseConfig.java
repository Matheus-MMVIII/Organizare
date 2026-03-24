package com.organizare.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public final class DatabaseConfig {
    // Impede a instanciacao da classe utilitaria de acesso ao banco.
    private DatabaseConfig() {
    }

    // Cria e retorna uma nova conexao JDBC usando as variaveis de ambiente obrigatorias.
    public static Connection getConnection() throws SQLException {
        String url = requireEnv("DB_URL");
        String user = requireEnv("DB_USER");
        String password = requireEnv("DB_PASSWORD");
        return DriverManager.getConnection(url, user, password);
    }

    // Busca uma variavel obrigatoria e falha explicitamente quando ela nao existe.
    private static String requireEnv(String key) {
        String value = EnvConfig.get(key);
        if (value == null || value.isBlank()) {
            throw new IllegalStateException("Variavel de ambiente ausente: " + key);
        }
        return value;
    }
}
