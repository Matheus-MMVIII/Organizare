package com.organizare.service;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.DateTimeException;
import java.time.MonthDay;
import java.util.List;
import java.util.Map;

import com.organizare.config.DatabaseConfig;
import com.organizare.exception.ConflictException;
import com.organizare.exception.NotFoundException;
import com.organizare.exception.ValidationException;
import com.organizare.model.User;
import com.organizare.repository.UserRepository;
import com.organizare.validation.RequestValidator;

public class UserService {
    private final UserRepository userRepository;

    // Recebe o repositorio usado para acessar e persistir dados de usuarios.
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // Lista todos os usuarios cadastrados abrindo e fechando a conexao automaticamente.
    public List<User> listAll() throws SQLException {
        try (Connection connection = DatabaseConfig.getConnection()) {
            return userRepository.listAll(connection);
        }
    }

    // Busca um usuario pelo ID e gera erro 404 quando ele nao existe.
    public User findById(int id) throws SQLException {
        try (Connection connection = DatabaseConfig.getConnection()) {
            return userRepository.findById(connection, id)
                    .orElseThrow(() -> new NotFoundException("Usuario nao encontrado."));
        }
    }

    // Valida os dados recebidos, garante unicidade do email e cria um novo usuario.
    public User create(Map<String, String> payload) throws SQLException {
        String name = RequestValidator.requireText(payload, "name", 2, 100);
        String email = RequestValidator.requireEmail(payload, "email");
        String cellPhone = RequestValidator.requirePhone(payload, "cellPhone");
        int birthMonth = RequestValidator.requireInt(payload, "birthMonth", 1, 12);
        int birthDay = RequestValidator.requireInt(payload, "birthDay", 1, 31);
        validateBirthDate(birthMonth, birthDay);

        try (Connection connection = DatabaseConfig.getConnection()) {
            ensureEmailIsUnique(connection, email, null);
            return userRepository.insert(connection, new User(0, name, email, cellPhone, birthMonth, birthDay));
        }
    }

    // Valida os dados recebidos, verifica existencia do usuario e atualiza seu cadastro.
    public User update(int id, Map<String, String> payload) throws SQLException {
        String name = RequestValidator.requireText(payload, "name", 2, 100);
        String email = RequestValidator.requireEmail(payload, "email");
        String cellPhone = RequestValidator.requirePhone(payload, "cellPhone");
        int birthMonth = RequestValidator.requireInt(payload, "birthMonth", 1, 12);
        int birthDay = RequestValidator.requireInt(payload, "birthDay", 1, 31);
        validateBirthDate(birthMonth, birthDay);

        try (Connection connection = DatabaseConfig.getConnection()) {
            if (userRepository.findById(connection, id).isEmpty()) {
                throw new NotFoundException("Usuario nao encontrado.");
            }
            ensureEmailIsUnique(connection, email, id);
            return userRepository.update(connection, new User(id, name, email, cellPhone, birthMonth, birthDay));
        }
    }

    // Remove um usuario pelo ID e falha com 404 quando o registro nao existe.
    public void delete(int id) throws SQLException {
        try (Connection connection = DatabaseConfig.getConnection()) {
            boolean deleted = userRepository.delete(connection, id);
            if (!deleted) {
                throw new NotFoundException("Usuario nao encontrado.");
            }
        }
    }

    // Garante que nenhum outro usuario use o mesmo email antes de salvar a alteracao.
    private void ensureEmailIsUnique(Connection connection, String email, Integer currentUserId) throws SQLException {
        userRepository.findByEmail(connection, email).ifPresent(existingUser -> {
            if (currentUserId == null || existingUser.getId() != currentUserId.intValue()) {
                throw new ConflictException("Ja existe um usuario com este email.");
            }
        });
    }

    // Valida se a combinacao de mes e dia forma uma data de aniversario possivel.
    private void validateBirthDate(int birthMonth, int birthDay) {
        try {
            MonthDay.of(birthMonth, birthDay);
        } catch (DateTimeException ex) {
            throw new ValidationException("Data de aniversario invalida.");
        }
    }
}
