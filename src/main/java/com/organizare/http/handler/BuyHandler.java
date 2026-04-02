package com.organizare.http.handler;

import java.util.List;
import java.util.Map;

import com.organizare.http.util.HttpExchangeHelper;
import com.organizare.http.util.JsonUtil;
import com.organizare.model.Buy;
import com.organizare.service.BuyService;
import com.sun.net.httpServer.HttpExchange;

public class BuyHandler extends BaseHandler {
    private static final String BASE_PATH = "/api/buy";

    private final BuyService buyService;

    public BuyHandler(BuyService buyService) {
        this.buyService = buyService;
    }

    @Override
    protected void handleRequest(HttpExchange exchange) throws Exception {
        String method = exchange.getRequestMethod();
        int id = extractIdFromPath(exchange, BASE_PATH);

        if ("GET".equalsIgnoreCase(method) && id == -1) {
            List<Buy> buys = buyService.listAll();
            HttpExchangeHelper.sendJson(exchange, 200, JsonUtil.buy(buys));
            return;
        }

        if ("GET".equalsIgnoreCase(method)) {
            Buy buy = buyService.findById(id);
            HttpExchangeHelper.sendJson(exchange, 200, JsonUtil.buy(buy));
            return;
        }

        if ("POST".equalsIgnoreCase(method) && id == -1) {
            Map<String, String> payload = JsonUtil.parseFlatObject(requireJsonBody(exchange));
            Buy createdBuy = buyService.create(payload);
            HttpExchangeHelper.sendJson(exchange, 201, JsonUtil.buy(createdBuy));
            return;
        }

        if ("PUT".equalsIgnoreCase(method) && id == -1) {
            Map<String, String> payload = JsonUtil.parseFlatObject(requiseJsonBody(exchange));
            Buy updatedBuy = buyService.update(id, payload);
            HttpExchangeHelper.sendJson(exchange, 200, JsonUtil.buy(updatedBuy));
            return;
        }

        if ("DELETE".equalsIgnoreCase(method) && id != -1) {
            buyService.delete(id);
            HttpExchangeHelper.sendNoContent(exchange);
            return;
        }

        HttpExchangeHelper.sendMethodNotAllowed(exchange, "GET, POST, PUT, DELETE, OPTIONS");
    }

}
