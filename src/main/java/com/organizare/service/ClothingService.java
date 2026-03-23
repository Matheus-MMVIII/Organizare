package com.organizare.service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.organizare.config.DatabaseConfig;
import com.organizare.exception.NotFoundException;
import com.organizare.model.Clothing;
import com.organizare.repository.ClothingRepository;
import com.organizare.validation.RequestValidator;

public class ClothingService {
    private final ClothingRepository clothingRepository;

    public ClothingService(ClothingRepository clothingRepository) {
        this.clothingRepository = clothingRepository;
    }

    public List<Clothing> listAll() throws SQLException {
        try (Connection connection = DatabaseConfig.getConnection()) {
            return clothingRepository.listAll(connection);
        }
    }

    public Clothing findById(int id) throws SQLException {
        try (Connection connection = DatabaseConfig.getConnection()) {
            return clothingRepository.findById(connection, id)
                    .orElseThrow(() -> new NotFoundException("Peca de roupa nao encontrada."));
        }
    }

    public Clothing create(Map<String, String> payload) throws SQLException {
        String name = RequestValidator.requireText(payload, "name", 2, 100);
        double price = RequestValidator.requireDecimal(payload, "price", 0.0, 1000000.0);
        int stock = RequestValidator.requireInt(payload, "stock", 0, 1000000);
        String size = RequestValidator.requireText(payload, "size", 1, 10);
        String color = RequestValidator.requireText(payload, "color", 2, 50);

        try (Connection connection = DatabaseConfig.getConnection()) {
            return clothingRepository.insert(connection, new Clothing(0, name, price, stock, size, color));
        }
    }

    public Clothing update(int id, Map<String, String> payload) throws SQLException {
        String name = RequestValidator.requireText(payload, "name", 2, 100);
        double price = RequestValidator.requireDecimal(payload, "price", 0.0, 1000000.0);
        int stock = RequestValidator.requireInt(payload, "stock", 0, 1000000);
        String size = RequestValidator.requireText(payload, "size", 1, 10);
        String color = RequestValidator.requireText(payload, "color", 2, 50);

        try (Connection connection = DatabaseConfig.getConnection()) {
            if (clothingRepository.findById(connection, id).isEmpty()) {
                throw new NotFoundException("Peca de roupa nao encontrada.");
            }
            return clothingRepository.update(connection, new Clothing(id, name, price, stock, size, color));
        }
    }

    public void delete(int id) throws SQLException {
        try (Connection connection = DatabaseConfig.getConnection()) {
            boolean deleted = clothingRepository.delete(connection, id);
            if (!deleted) {
                throw new NotFoundException("Peca de roupa nao encontrada.");
            }
        }
    }
}
