package com.organizare.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.organizare.model.Clothing;

public class ClothingRepository {
    public void insert(Connection connection, Clothing clothing) throws SQLException {
        String sql = "INSERT INTO clothing (name, price, stock, size, color) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, clothing.getName());
            statement.setDouble(2, clothing.getPrice());
            statement.setInt(3, clothing.getStock());
            statement.setString(4, clothing.getSize());
            statement.setString(5, clothing.getColor());
            statement.executeUpdate();
        }
    }

    public void readById(Connection connection, int clothingId) throws SQLException {
        String sql = "SELECT * FROM clothing WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, clothingId);
            statement.executeQuery();
        }
    }

    public void updatePrice(Connection connection, int clothingId, double newPrice) throws SQLException {
        String sql = "UPDATE clothing SET price = ? WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setDouble(1, newPrice);
            statement.setInt(2, clothingId);
            statement.executeUpdate();
        }
    }

    public void delete(Connection connection, int clothingId) throws SQLException {
        String sql = "DELETE FROM clothing WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, clothingId);
            statement.executeUpdate();
        }
    }

    public void listAll(Connection connection) throws SQLException {
        String sql = "SELECT * FROM clothing";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.executeQuery();
        }
    }
}
