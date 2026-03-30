package com.organizare.config;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public final class DatabaseMigration {
    private DatabaseMigration() {
    }

    public static void ensureUserCreatedAtColumn() {
        String sql = "ALTER TABLE IF EXISTS users "
                + "ADD COLUMN IF NOT EXISTS created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP";

        try (Connection connection = DatabaseConfig.getConnection();
                Statement statement = connection.createStatement()) {
            statement.executeUpdate(sql);
        } catch (SQLException exception) {
            throw new IllegalStateException("Nao foi possivel preparar a coluna created_at dos usuarios.", exception);
        }
    }
}
