package com.organizare;

import java.sql.Connection;
import java.sql.SQLException;

import com.organizare.config.DatabaseConfig;
import com.organizare.model.Clothing;
import com.organizare.model.User;
import com.organizare.repository.ClothingRepository;
import com.organizare.repository.UserRepository;

public class App {

    public static void main(String[] args) {
        UserRepository userRepository = new UserRepository();
        ClothingRepository clothingRepository = new ClothingRepository();

        try (Connection connection = DatabaseConfig.getConnection()) {
            System.out.println("Conectado ao banco!");

            User user = new User(1, "John Doe", "john.doe@example.com", "123-456-7890", 5, 15);
            Clothing shirt = new Clothing(1, "T-Shirt", 19.99, 50, "M", "Red");

            userRepository.insert(connection, user);
            clothingRepository.insert(connection, shirt);
        } catch (SQLException | IllegalStateException e) {
            e.printStackTrace();
        }
    }
}
