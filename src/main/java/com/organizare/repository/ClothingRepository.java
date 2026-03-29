package com.organizare.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.organizare.model.Clothing;

public class ClothingRepository {
    // Insere uma nova roupa no banco e devolve a entidade com o ID gerado.
    public Clothing insert(Connection connection, Clothing clothing) throws SQLException {
        String sql = "INSERT INTO clothing (name, price, stock, size, color) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, clothing.getName());
            statement.setDouble(2, clothing.getPrice());
            statement.setInt(3, clothing.getStock());
            statement.setString(4, clothing.getSizeAsString());
            statement.setString(5, clothing.getColor());
            statement.executeUpdate();
            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return new Clothing(
                            generatedKeys.getInt(1),
                            clothing.getName(),
                            clothing.getPrice(),
                            clothing.getStock(),
                            clothing.getSizeAsString(),
                            clothing.getColor());
                }
            }
        }
        throw new SQLException("Falha ao gerar o identificador da roupa.");
    }

    // Busca uma roupa pelo ID e retorna Optional para tratar ausencia de resultado.
    public Optional<Clothing> findById(Connection connection, int clothingId) throws SQLException {
        String sql = "SELECT * FROM clothing WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, clothingId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(mapRow(resultSet));
                }
                return Optional.empty();
            }
        }
    }

    // Atualiza os dados de uma roupa existente e retorna a propria entidade
    public Clothing update(Connection connection, Clothing clothing) throws SQLException {
        String sql = "UPDATE clothing SET name = ?, price = ?, stock = ?, size = ?, color = ? WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, clothing.getName());
            statement.setDouble(2, clothing.getPrice());
            statement.setInt(3, clothing.getStock());
            statement.setString(4, clothing.getSizeAsString());
            statement.setString(5, clothing.getColor());
            statement.setInt(6, clothing.getId());
            statement.executeUpdate();
        }
        return clothing;
    }

    // Remove uma roupa pelo ID e informa se alguma linha foi realmente excluida.
    public boolean delete(Connection connection, int clothingId) throws SQLException {
        String sql = "DELETE FROM clothing WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, clothingId);
            return statement.executeUpdate() > 0;
        }
    }

    // Lista todas as roupas cadastradas em ordem crescente de identificador.
    public List<Clothing> listAll(Connection connection) throws SQLException {
        String sql = "SELECT * FROM clothing ORDER BY id";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            try (ResultSet resultSet = statement.executeQuery()) {
                List<Clothing> clothingItems = new ArrayList<>();
                while (resultSet.next()) {
                    clothingItems.add(mapRow(resultSet));
                }
                return clothingItems;
            }
        }
    }

    // Converte uma linha retornada pelo banco em um objeto Clothing da aplicacao.
    private Clothing mapRow(ResultSet resultSet) throws SQLException {
        return new Clothing(
                resultSet.getInt("id"),
                resultSet.getString("name"),
                resultSet.getDouble("price"),
                resultSet.getInt("stock"),
                resultSet.getString("size"),
                resultSet.getString("color"));
    }
}
