package com.organizare.service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.organizare.config.DatabaseConfig;
import com.organizare.exception.NotFoundException;
import com.organizare.model.Buy;
import com.organizare.repository.BuyRepository;
import com.organizare.validation.RequestValidator;

public class BuyService {
    private final BuyRepository buyRepository;

    public BuyService(BuyRepository buyRepository) {
        this.buyRepository = buyRepository;
    }

    public List<Buy> listAll() throws SQLException {
        try (Connection connection = DatabaseConfig.getConnection()) {
            return buyRepository.listAll(connection);
        }
    }

    public Buy findById(int id) throws SQLException {
        try (Connection connection = DatabaseConfig.getConnection()) {
            return buyRepository.findById(connection, id)
                    .orElseThrow(() -> new NotFoundException("Compra não encontrada."));
        }
    }

    public Buy create(Map<String, String> payload) throws SQLException {
        int user_id = RequestValidator.requireInt(payload, "user_id", 0, 1000000);
        int product_id = RequestValidator.requireInt(payload, "product_id", 0, 1000000);
        int quantity = RequestValidator.requireInt(payload, "quantity", 0, 1000000);
        Double total_price = RequestValidator.requireDecimal(payload, "total_price", 0, 1000000.0);

        try (Connection connection = DatabaseConfig.getConnection()) {
            return buyRepository.insert(connection, new Buy(0, user_id, product_id, quantity, total_price));
        }
    }

    public Buy update(int id, Map<String, String> payload) throws SQLException {
        int user_id = RequestValidator.requireInt(payload, "user_id", 0, 1000000);
        int product_id = RequestValidator.requireInt(payload, "product_id", 0, 1000000);
        int quantity = RequestValidator.requireInt(payload, "quantity", 0, 1000000);
        Double total_price = RequestValidator.requireDecimal(payload, "total_price", 0, 1000000.0);

        try (Connection connection = DatabaseConfig.getConnection()) {
            Buy existingBuy = buyRepository.findById(connection, id)
                    .orElseThrow(() -> new NotFoundException("Compra nao encontrado."));
            return buyRepository.Update(connection,
                    new Buy(id, user_id, product_id, quantity, total_price, existingBuy.getCreatedAt()));
        }
    }

    public void delete(int id) throws SQLException {
        try (Connection connection = DatabaseConfig.getConnection()) {
            boolean deleted = buyRepository.delete(connection, id);
            if (!deleted) {
                throw new NotFoundException("Compra nao encontrada.");
            }
        }
    }
}
