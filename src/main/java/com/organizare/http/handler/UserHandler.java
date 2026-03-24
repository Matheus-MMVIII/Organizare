package com.organizare.http.handler;

import java.util.List;
import java.util.Map;

import com.organizare.http.util.HttpExchangeHelper;
import com.organizare.http.util.JsonUtil;
import com.organizare.model.User;
import com.organizare.service.UserService;
import com.sun.net.httpserver.HttpExchange;

public class UserHandler extends BaseHandler {
    private static final String BASE_PATH = "/api/users";

    private final UserService userService;

    // Recebe o service responsavel pelas regras de negocio relacionadas a usuarios.
    public UserHandler(UserService userService) {
        this.userService = userService;
    }

    // Direciona cada requisicao de usuario para a operacao correta com base no metodo HTTP e na URL.
    @Override
    protected void handleRequest(HttpExchange exchange) throws Exception {
        String method = exchange.getRequestMethod();
        int id = extractIdFromPath(exchange, BASE_PATH);

        if ("GET".equalsIgnoreCase(method) && id == -1) {
            List<User> users = userService.listAll();
            HttpExchangeHelper.sendJson(exchange, 200, JsonUtil.users(users));
            return;
        }

        if ("GET".equalsIgnoreCase(method)) {
            User user = userService.findById(id);
            HttpExchangeHelper.sendJson(exchange, 200, JsonUtil.user(user));
            return;
        }

        if ("POST".equalsIgnoreCase(method) && id == -1) {
            Map<String, String> payload = JsonUtil.parseFlatObject(requireJsonBody(exchange));
            User createdUser = userService.create(payload);
            HttpExchangeHelper.sendJson(exchange, 201, JsonUtil.user(createdUser));
            return;
        }

        if ("PUT".equalsIgnoreCase(method) && id != -1) {
            Map<String, String> payload = JsonUtil.parseFlatObject(requireJsonBody(exchange));
            User updatedUser = userService.update(id, payload);
            HttpExchangeHelper.sendJson(exchange, 200, JsonUtil.user(updatedUser));
            return;
        }

        if ("DELETE".equalsIgnoreCase(method) && id != -1) {
            userService.delete(id);
            HttpExchangeHelper.sendNoContent(exchange);
            return;
        }

        HttpExchangeHelper.sendMethodNotAllowed(exchange, "GET, POST, PUT, DELETE, OPTIONS");
    }
}
