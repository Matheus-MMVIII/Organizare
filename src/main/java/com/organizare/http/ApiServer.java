package com.organizare.http;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.organizare.config.AppConfig;
import com.organizare.http.handler.ClothingHandler;
import com.organizare.http.handler.HealthHandler;
import com.organizare.http.handler.UserHandler;
import com.organizare.repository.ClothingRepository;
import com.organizare.repository.UserRepository;
import com.organizare.service.ClothingService;
import com.organizare.service.UserService;
import com.sun.net.httpserver.HttpServer;

public class ApiServer {
    private final HttpServer server;
    private final ExecutorService executor;

    public ApiServer() throws IOException {
        this.server = HttpServer.create(new InetSocketAddress(AppConfig.getPort()), AppConfig.getBacklog());
        this.executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        registerContexts();
        server.setExecutor(executor);
        Runtime.getRuntime().addShutdownHook(new Thread(this::stop));
    }

    public void start() {
        server.start();
        System.out.println("Servidor iniciado na porta " + AppConfig.getPort());
    }

    public void stop() {
        server.stop(0);
        executor.shutdown();
    }

    private void registerContexts() {
        UserService userService = new UserService(new UserRepository());
        ClothingService clothingService = new ClothingService(new ClothingRepository());

        server.createContext("/health", new HealthHandler());
        server.createContext("/api/users", new UserHandler(userService));
        server.createContext("/api/clothing", new ClothingHandler(clothingService));
    }
}
