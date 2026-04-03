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
        String sql = "INSERT INTO orders (user_id, clothing_id, quantity, total_price) VALUES (?, ?, ?, ?) "
                + "RETURNING id, user_id, clothing_id, quantity, total_price, order_date";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, buy.getUserId());
            statement.setInt(2, buy.getClothingId());
            statement.setInt(3, buy.getQuantity());
            statement.setDouble(4, buy.getTotalPrice());
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return mapRow(resultSet);
                }
            }
        }
        throw new SQLException("Falha ao gerar o identificador da compra.");
    }

    public Optional<Buy> findById(Connection connection, int buyId) throws SQLException {
        String sql = "SELECT o.id, o.user_id, o.clothing_id, o.quantity, o.total_price, o.order_date, "
                + "u.name AS user_name, c.name AS clothing_name "
                + "FROM orders o "
                + "JOIN users u ON u.id = o.user_id "
                + "JOIN clothing c ON c.id = o.clothing_id "
                + "WHERE o.id = ?";
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

    public Buy update(Connection connection, Buy buy) throws SQLException {
        String sql = "UPDATE orders SET user_id = ?, clothing_id = ?, quantity = ?, total_price = ? WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, buy.getUserId());
            statement.setInt(2, buy.getClothingId());
            statement.setInt(3, buy.getQuantity());
            statement.setDouble(4, buy.getTotalPrice());
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

    public boolean existsByUserId(Connection connection, int userId) throws SQLException {
        String sql = "SELECT 1 FROM orders WHERE user_id = ? LIMIT 1";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, userId);
            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next();
            }
        }
    }

    public boolean existsByClothingId(Connection connection, int clothingId) throws SQLException {
        String sql = "SELECT 1 FROM orders WHERE clothing_id = ? LIMIT 1";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, clothingId);
            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next();
            }
        }
    }

    public List<Buy> listAll(Connection connection) throws SQLException {
        String sql = "SELECT o.id, o.user_id, o.clothing_id, o.quantity, o.total_price, o.order_date, "
                + "u.name AS user_name, c.name AS clothing_name "
                + "FROM orders o "
                + "JOIN users u ON u.id = o.user_id "
                + "JOIN clothing c ON c.id = o.clothing_id "
                + "ORDER BY o.order_date DESC, o.id DESC";
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
                resultSet.getInt("clothing_id"),
                resultSet.getInt("quantity"),
                resultSet.getDouble("total_price"),
                readOrderDate(resultSet),
                readOptionalText(resultSet, "user_name"),
                readOptionalText(resultSet, "clothing_name"));
    }

    private java.time.LocalDateTime readOrderDate(ResultSet resultSet) throws SQLException {
        Timestamp orderDate = resultSet.getTimestamp("order_date");
        return orderDate == null ? null : orderDate.toLocalDateTime();
    }

    private String readOptionalText(ResultSet resultSet, String columnName) throws SQLException {
        try {
            return resultSet.getString(columnName);
        } catch (SQLException exception) {
            return null;
        }
    }
}
