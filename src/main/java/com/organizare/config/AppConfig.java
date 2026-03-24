package com.organizare.config;

public final class AppConfig {
    private static final int DEFAULT_PORT = 8080;
    private static final int DEFAULT_BACKLOG = 50;
    private static final int DEFAULT_MAX_REQUEST_BODY_BYTES = 8192;
    private static final String DEFAULT_ALLOWED_ORIGIN = "http://localhost:5173";

    // Impede a instanciacao da classe utilitaria de configuracoes da aplicacao.
    private AppConfig() {
    }

    // Retorna a porta do servidor, usando valor padrao quando a variavel nao estiver definida.
    public static int getPort() {
        return getInt("PORT", DEFAULT_PORT);
    }

    // Retorna o backlog de conexoes pendentes aceitas pelo servidor HTTP.
    public static int getBacklog() {
        return getInt("SERVER_BACKLOG", DEFAULT_BACKLOG);
    }

    // Retorna o tamanho maximo permitido para o corpo das requisicoes.
    public static int getMaxRequestBodyBytes() {
        return getInt("MAX_REQUEST_BODY_BYTES", DEFAULT_MAX_REQUEST_BODY_BYTES);
    }

    // Retorna a origem permitida para requisicoes CORS.
    public static String getAllowedOrigin() {
        return getString("ALLOWED_ORIGIN", DEFAULT_ALLOWED_ORIGIN);
    }

    // Le uma configuracao inteira e aplica fallback caso o valor esteja ausente ou invalido.
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

    // Le uma configuracao de texto e aplica fallback quando o valor nao estiver disponivel.
    private static String getString(String key, String fallback) {
        String value = EnvConfig.get(key);
        return value == null || value.isBlank() ? fallback : value;
    }
}
