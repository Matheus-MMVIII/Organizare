package com.organizare.http.handler;

import java.util.List;
import java.util.Map;

import com.organizare.http.util.HttpExchangeHelper;
import com.organizare.http.util.JsonUtil;
import com.organizare.model.Clothing;
import com.organizare.service.ClothingService;
import com.sun.net.httpserver.HttpExchange;

public class ClothingHandler extends BaseHandler {
    private static final String BASE_PATH = "/api/clothing";

    private final ClothingService clothingService;

    // Recebe o service responsavel pelas regras de negocio relacionadas a roupas.
    public ClothingHandler(ClothingService clothingService) {
        this.clothingService = clothingService;
    }

    // Direciona cada requisicao de roupa para a operacao correta com base no metodo HTTP e na URL.
    @Override
    protected void handleRequest(HttpExchange exchange) throws Exception {
        String method = exchange.getRequestMethod();
        int id = extractIdFromPath(exchange, BASE_PATH);

        if ("GET".equalsIgnoreCase(method) && id == -1) {
            List<Clothing> clothingItems = clothingService.listAll();
            HttpExchangeHelper.sendJson(exchange, 200, JsonUtil.clothingItems(clothingItems));
            return;
        }

        if ("GET".equalsIgnoreCase(method)) {
            Clothing clothing = clothingService.findById(id);
            HttpExchangeHelper.sendJson(exchange, 200, JsonUtil.clothing(clothing));
            return;
        }

        if ("POST".equalsIgnoreCase(method) && id == -1) {
            Map<String, String> payload = JsonUtil.parseFlatObject(requireJsonBody(exchange));
            Clothing createdClothing = clothingService.create(payload);
            HttpExchangeHelper.sendJson(exchange, 201, JsonUtil.clothing(createdClothing));
            return;
        }

        if ("PUT".equalsIgnoreCase(method) && id != -1) {
            Map<String, String> payload = JsonUtil.parseFlatObject(requireJsonBody(exchange));
            Clothing updatedClothing = clothingService.update(id, payload);
            HttpExchangeHelper.sendJson(exchange, 200, JsonUtil.clothing(updatedClothing));
            return;
        }

        if ("DELETE".equalsIgnoreCase(method) && id != -1) {
            clothingService.delete(id);
            HttpExchangeHelper.sendNoContent(exchange);
            return;
        }

        HttpExchangeHelper.sendMethodNotAllowed(exchange, "GET, POST, PUT, DELETE, OPTIONS");
    }
}
