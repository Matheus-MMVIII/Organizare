package com.organizare.http;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.organizare.config.AppConfig;
import com.organizare.config.DatabaseMigration;
import com.organizare.http.handler.ClothingHandler;
import com.organizare.http.handler.HealthHandler;
import com.organizare.http.handler.UserHandler;
import com.organizare.http.handler.BuyHandler;
import com.organizare.repository.ClothingRepository;
import com.organizare.repository.UserRepository;
import com.organizare.repository.BuyRepository;
import com.organizare.service.ClothingService;
import com.organizare.service.UserService;
import com.organizare.service.BuyService;
import com.sun.net.httpserver.HttpServer;

public class ApiServer {
    private final HttpServer server;
    private final ExecutorService executor;

    // Configura o servidor HTTP, a pool de threads, as rotas e o desligamento
    // controlado.
    public ApiServer() throws IOException {
        this.server = HttpServer.create(new InetSocketAddress(AppConfig.getPort()), AppConfig.getBacklog());
        this.executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        registerContexts();
        server.setExecutor(executor);
        Runtime.getRuntime().addShutdownHook(new Thread(this::stop));
    }

    // Inicia o servidor e registra no console a porta utilizada.
    public void start() {
        server.start();
        System.out.println("Servidor iniciado na porta " + AppConfig.getPort());
    }

    // Encerra o servidor HTTP e libera a pool de threads usada para processar
    // requisicoes.
    public void stop() {
        server.stop(0);
        executor.shutdown();
    }

    // Cria as dependencias principais e associa cada rota ao seu respectivo
    // handler.
    private void registerContexts() {
        DatabaseMigration.ensureUserCreatedAtColumn();
        DatabaseMigration.ensureOrdersTable();

        UserRepository userRepository = new UserRepository();
        ClothingRepository clothingRepository = new ClothingRepository();
        BuyRepository buyRepository = new BuyRepository();

        UserService userService = new UserService(userRepository, buyRepository);
        ClothingService clothingService = new ClothingService(clothingRepository, buyRepository);
        BuyService buyService = new BuyService(buyRepository, userRepository, clothingRepository);

        server.createContext("/health", new HealthHandler());
        server.createContext("/api/users", new UserHandler(userService));
        server.createContext("/api/clothing", new ClothingHandler(clothingService));
        server.createContext("/api/buy", new BuyHandler(buyService));
    }
}
