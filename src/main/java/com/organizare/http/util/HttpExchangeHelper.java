package com.organizare.http.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

import com.organizare.config.AppConfig;
import com.organizare.exception.BadRequestException;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;

public final class HttpExchangeHelper {
    // Impede a instanciacao da classe utilitaria de manipulacao HTTP.
    private HttpExchangeHelper() {
    }

    // Aplica headers padrao de resposta, incluindo CORS, seguranca e politicas de cache.
    public static void applyDefaultHeaders(HttpExchange exchange) {
        Headers headers = exchange.getResponseHeaders();
        headers.set("Content-Type", "application/json; charset=utf-8");
        headers.set("Access-Control-Allow-Origin", AppConfig.getAllowedOrigin());
        headers.set("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        headers.set("Access-Control-Allow-Headers", "Content-Type");
        headers.set("X-Content-Type-Options", "nosniff");
        headers.set("X-Frame-Options", "DENY");
        headers.set("Referrer-Policy", "no-referrer");
        headers.set("Cache-Control", "no-store");
    }

    // Le o corpo da requisicao respeitando o limite maximo configurado para evitar abusos.
    public static String readRequestBody(HttpExchange exchange) throws IOException {
        int maxBytes = AppConfig.getMaxRequestBodyBytes();
        try (InputStream inputStream = exchange.getRequestBody();
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            byte[] buffer = new byte[1024];
            int bytesRead;
            int totalBytes = 0;

            while ((bytesRead = inputStream.read(buffer)) != -1) {
                totalBytes += bytesRead;
                if (totalBytes > maxBytes) {
                    throw new BadRequestException("Corpo da requisicao excede o limite permitido.");
                }
                outputStream.write(buffer, 0, bytesRead);
            }

            return outputStream.toString(StandardCharsets.UTF_8);
        }
    }

    // Envia uma resposta JSON com o status informado e o corpo serializado em UTF-8.
    public static void sendJson(HttpExchange exchange, int statusCode, String json) throws IOException {
        byte[] responseBytes = json.getBytes(StandardCharsets.UTF_8);
        exchange.sendResponseHeaders(statusCode, responseBytes.length);
        try (OutputStream responseBody = exchange.getResponseBody()) {
            responseBody.write(responseBytes);
        }
    }

    // Envia uma resposta sem corpo, usada normalmente para operacoes que nao retornam conteudo.
    public static void sendNoContent(HttpExchange exchange) throws IOException {
        exchange.sendResponseHeaders(204, -1);
    }

    // Retorna erro 405 e informa explicitamente quais metodos a rota aceita.
    public static void sendMethodNotAllowed(HttpExchange exchange, String allowedMethods) throws IOException {
        exchange.getResponseHeaders().set("Allow", allowedMethods);
        sendJson(exchange, 405, JsonUtil.error("Metodo nao permitido."));
    }
}
