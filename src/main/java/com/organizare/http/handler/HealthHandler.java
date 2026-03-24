package com.organizare.http.handler;

import com.organizare.http.util.HttpExchangeHelper;
import com.organizare.http.util.JsonUtil;
import com.sun.net.httpserver.HttpExchange;

public class HealthHandler extends BaseHandler {
    // Responde ao endpoint de saude informando se a API esta ativa.
    @Override
    protected void handleRequest(HttpExchange exchange) throws Exception {
        if (!"GET".equalsIgnoreCase(exchange.getRequestMethod())) {
            HttpExchangeHelper.sendMethodNotAllowed(exchange, "GET, OPTIONS");
            return;
        }

        HttpExchangeHelper.sendJson(exchange, 200, JsonUtil.object("status", "ok"));
    }
}
