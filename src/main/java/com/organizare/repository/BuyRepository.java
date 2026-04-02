package com.organizare.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.organizare.model.Buy;

public class BuyRepository {
    public Buy insert(Connection connection, Buy buy) throws SQLException {
        String sql = "INSERT INTO orders (user_id, product_id, quantity, total_price) VALUES (?, ?, ?, ?) RETURNING *";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, buy.getUserId());
            statement.setInt(2, buy.getProductId());
            statement.setInt(3, buy.getQuantity());
            statement.setDouble(4, buy.getProductPrice());
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return mapRow(resultSet);
                }
            }
        }
        throw new SQLException("Falha ao gerar o indentificador da compra.");
    }

    public Optional<Buy> findById(Connection connection, int buyId) throws SQLException {
        String sql = "SELECT * FROM orders WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, buyId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(mapRow(resultSet));
                }
                return Optional.empty();
            }
        }
    }

    public Optional<Buy> findByUserId(Connection connection, int userId) throws SQLException {
        String sql = "SELECT * FROM orders WHERE user_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, userId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(mapRow(resultSet));
                }
                return Optional.empty();
            }
        }
    }

    public Optional<Buy> findByProductId(Connection connection, int productId) throws SQLException {
        String sql = "SELECT * FROM orders WHERE product_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, productId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(mapRow(resultSet));
                }
                return Optional.empty();
            }
        }
    }

    public Buy Update(Connection connection, Buy buy) throws SQLException {
        String sql = "UPDATE orders SET user_id = ?, product_id = ?, quantity = ?, total_price = ? WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, buy.getUserId());
            statement.setInt(2, buy.getProductId());
            statement.setInt(3, buy.getQuantity());
            statement.setDouble(4, buy.getProductPrice());
            statement.setInt(5, buy.getId());
            statement.executeUpdate();
        }
        return buy;
    }

    public boolean delete(Connection connection, int buyId) throws SQLException {
        String sql = "DELETE FROM orders WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, buyId);
            return statement.executeUpdate() > 0;
        }
    }

    public List<Buy> listAll(Connection connection) throws SQLException {
        String sql = "SELECT * FROM orders ORDER BY id";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            try (ResultSet resultSet = statement.executeQuery()) {
                List<Buy> buys = new ArrayList<>();
                while (resultSet.next()) {
                    buys.add(mapRow(resultSet));
                }
                return buys;
            }
        }
    }

    public Buy mapRow(ResultSet resultSet) throws SQLException {
        return new Buy(
                resultSet.getInt("id"),
                resultSet.getInt("user_id"),
                resultSet.getInt("product_id"),
                resultSet.getInt("quantity"),
                resultSet.getDouble("total_price"),
                readCreatedAt(resultSet));
    }

    private java.time.LocalDateTime readCreatedAt(ResultSet resultSet) throws SQLException {
        Timestamp createdAt = resultSet.getTimestamp("created_at");
        return createdAt == null ? null : createdAt.toLocalDateTime();
    }
}
