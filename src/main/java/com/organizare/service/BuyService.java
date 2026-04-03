package com.organizare.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.organizare.config.DatabaseConfig;
import com.organizare.exception.ConflictException;
import com.organizare.exception.NotFoundException;
import com.organizare.model.Clothing;
import com.organizare.model.Buy;
import com.organizare.model.User;
import com.organizare.repository.BuyRepository;
import com.organizare.repository.ClothingRepository;
import com.organizare.repository.UserRepository;
import com.organizare.validation.RequestValidator;

public class BuyService {
    private final BuyRepository buyRepository;
    private final UserRepository userRepository;
    private final ClothingRepository clothingRepository;

    public BuyService(BuyRepository buyRepository, UserRepository userRepository, ClothingRepository clothingRepository) {
        this.buyRepository = buyRepository;
        this.userRepository = userRepository;
        this.clothingRepository = clothingRepository;
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
        int userId = requirePayloadInt(payload, "userId", "user_id");
        int clothingId = requirePayloadInt(payload, "clothingId", "clothing_id");
        int quantity = RequestValidator.requireInt(payload, "quantity", 1, 1000000);

        try (Connection connection = DatabaseConfig.getConnection()) {
            connection.setAutoCommit(false);
            try {
                User user = requireUser(connection, userId);
                Clothing clothing = requireClothing(connection, clothingId);

                ensureStock(clothing, quantity);
                persistClothingStock(connection, clothing, clothing.getStock() - quantity);

                double totalPrice = calculateTotalPrice(clothing.getPrice(), quantity);
                Buy insertedBuy = buyRepository.insert(connection, new Buy(0, user.getId(), clothing.getId(), quantity, totalPrice));
                Buy createdBuy = buyRepository.findById(connection, insertedBuy.getId())
                        .orElseThrow(() -> new NotFoundException("Compra não encontrada após o cadastro."));
                connection.commit();
                return createdBuy;
            } catch (RuntimeException | SQLException exception) {
                connection.rollback();
                throw exception;
            } finally {
                connection.setAutoCommit(true);
            }
        }
    }

    public Buy update(int id, Map<String, String> payload) throws SQLException {
        int userId = requirePayloadInt(payload, "userId", "user_id");
        int clothingId = requirePayloadInt(payload, "clothingId", "clothing_id");
        int quantity = RequestValidator.requireInt(payload, "quantity", 1, 1000000);

        try (Connection connection = DatabaseConfig.getConnection()) {
            connection.setAutoCommit(false);
            try {
                Buy existingBuy = buyRepository.findById(connection, id)
                        .orElseThrow(() -> new NotFoundException("Compra nao encontrada."));

                requireUser(connection, userId);

                Clothing previousClothing = requireClothing(connection, existingBuy.getClothingId());
                Clothing nextClothing = requireClothing(connection, clothingId);
                int availableStock = nextClothing.getId() == previousClothing.getId()
                        ? nextClothing.getStock() + existingBuy.getQuantity()
                        : nextClothing.getStock();

                if (availableStock < quantity) {
                    throw new ConflictException("Estoque insuficiente para concluir a compra.");
                }

                persistClothingStock(connection, previousClothing, previousClothing.getStock() + existingBuy.getQuantity());
                persistClothingStock(connection, nextClothing, availableStock - quantity);

                double totalPrice = calculateTotalPrice(nextClothing.getPrice(), quantity);
                buyRepository.update(connection,
                        new Buy(id, userId, clothingId, quantity, totalPrice, existingBuy.getOrderDate()));

                Buy updatedBuy = buyRepository.findById(connection, id)
                        .orElseThrow(() -> new NotFoundException("Compra nao encontrada apos a atualizacao."));
                connection.commit();
                return updatedBuy;
            } catch (RuntimeException | SQLException exception) {
                connection.rollback();
                throw exception;
            } finally {
                connection.setAutoCommit(true);
            }
        }
    }

    public void delete(int id) throws SQLException {
        try (Connection connection = DatabaseConfig.getConnection()) {
            connection.setAutoCommit(false);
            try {
                Buy existingBuy = buyRepository.findById(connection, id)
                        .orElseThrow(() -> new NotFoundException("Compra nao encontrada."));
                Clothing clothing = requireClothing(connection, existingBuy.getClothingId());

                persistClothingStock(connection, clothing, clothing.getStock() + existingBuy.getQuantity());

                boolean deleted = buyRepository.delete(connection, id);
                if (!deleted) {
                    throw new NotFoundException("Compra nao encontrada.");
                }

                connection.commit();
            } catch (RuntimeException | SQLException exception) {
                connection.rollback();
                throw exception;
            } finally {
                connection.setAutoCommit(true);
            }
        }
    }

    private int requirePayloadInt(Map<String, String> payload, String camelCaseField, String snakeCaseField) {
        if (payload.containsKey(camelCaseField)) {
            return RequestValidator.requireInt(payload, camelCaseField, 1, 1000000);
        }
        return RequestValidator.requireInt(payload, snakeCaseField, 1, 1000000);
    }

    private User requireUser(Connection connection, int userId) throws SQLException {
        return userRepository.findById(connection, userId)
                .orElseThrow(() -> new NotFoundException("Usuario da compra nao encontrado."));
    }

    private Clothing requireClothing(Connection connection, int clothingId) throws SQLException {
        return clothingRepository.findById(connection, clothingId)
                .orElseThrow(() -> new NotFoundException("Roupa da compra nao encontrada."));
    }

    private void ensureStock(Clothing clothing, int quantity) {
        if (clothing.getStock() < quantity) {
            throw new ConflictException("Estoque insuficiente para concluir a compra.");
        }
    }

    private double calculateTotalPrice(double unitPrice, int quantity) {
        return BigDecimal.valueOf(unitPrice)
                .multiply(BigDecimal.valueOf(quantity))
                .setScale(2, RoundingMode.HALF_UP)
                .doubleValue();
    }

    private void persistClothingStock(Connection connection, Clothing clothing, int nextStock) throws SQLException {
        clothingRepository.update(connection,
                new Clothing(clothing.getId(), clothing.getName(), clothing.getPrice(), nextStock,
                        clothing.getSizeAsString(), clothing.getColor()));
    }

}
