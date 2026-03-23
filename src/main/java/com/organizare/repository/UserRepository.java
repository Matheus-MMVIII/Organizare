package com.organizare.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.organizare.model.User;

public class UserRepository {
    public User insert(Connection connection, User user) throws SQLException {
        String sql = "INSERT INTO users (name, email, cellPhone, month, day) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, user.getName());
            statement.setString(2, user.getEmail());
            statement.setString(3, user.getCellPhone());
            statement.setInt(4, user.getBirthMonth());
            statement.setInt(5, user.getBirthDay());
            statement.executeUpdate();
            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return new User(
                            generatedKeys.getInt(1),
                            user.getName(),
                            user.getEmail(),
                            user.getCellPhone(),
                            user.getBirthMonth(),
                            user.getBirthDay());
                }
            }
        }
        throw new SQLException("Falha ao gerar o identificador do usuario.");
    }

    public Optional<User> findById(Connection connection, int userId) throws SQLException {
        String sql = "SELECT * FROM users WHERE id = ?";
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

    public Optional<User> findByEmail(Connection connection, String email) throws SQLException {
        String sql = "SELECT * FROM users WHERE email = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, email);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(mapRow(resultSet));
                }
                return Optional.empty();
            }
        }
    }

    public User update(Connection connection, User user) throws SQLException {
        String sql = "UPDATE users SET name = ?, email = ?, cellPhone = ?, month = ?, day = ? WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, user.getName());
            statement.setString(2, user.getEmail());
            statement.setString(3, user.getCellPhone());
            statement.setInt(4, user.getBirthMonth());
            statement.setInt(5, user.getBirthDay());
            statement.setInt(6, user.getId());
            statement.executeUpdate();
        }
        return user;
    }

    public boolean delete(Connection connection, int userId) throws SQLException {
        String sql = "DELETE FROM users WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, userId);
            return statement.executeUpdate() > 0;
        }
    }

    public List<User> listAll(Connection connection) throws SQLException {
        String sql = "SELECT * FROM users ORDER BY id";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            try (ResultSet resultSet = statement.executeQuery()) {
                List<User> users = new ArrayList<>();
                while (resultSet.next()) {
                    users.add(mapRow(resultSet));
                }
                return users;
            }
        }
    }

    private User mapRow(ResultSet resultSet) throws SQLException {
        return new User(
                resultSet.getInt("id"),
                resultSet.getString("name"),
                resultSet.getString("email"),
                resultSet.getString("cellphone"),
                resultSet.getInt("month"),
                resultSet.getInt("day"));
    }
}
