package com.organizare.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.organizare.model.User;

public class UserRepository {
    public void insert(Connection connection, User user) throws SQLException {
        String sql = "INSERT INTO users (name, email, cellPhone, month, day) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, user.getName());
            statement.setString(2, user.getEmail());
            statement.setString(3, user.getCellPhone());
            statement.setInt(4, user.getBirthMonth());
            statement.setInt(5, user.getBirthDay());
            statement.executeUpdate();
        }
    }

    public void readById(Connection connection, int userId) throws SQLException {
        String sql = "SELECT * FROM users WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, userId);
            statement.executeQuery();
        }
    }

    public void updateEmail(Connection connection, int userId, String newEmail) throws SQLException {
        String sql = "UPDATE users SET email = ? WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, newEmail);
            statement.setInt(2, userId);
            statement.executeUpdate();
        }
    }

    public void delete(Connection connection, int userId) throws SQLException {
        String sql = "DELETE FROM users WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, userId);
            statement.executeUpdate();
        }
    }

    public void listAll(Connection connection) throws SQLException {
        String sql = "SELECT * FROM users";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.executeQuery();
        }
    }
}
