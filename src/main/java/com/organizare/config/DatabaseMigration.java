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

    public static void ensureOrdersTable() {
        try (Connection connection = DatabaseConfig.getConnection();
                Statement statement = connection.createStatement()) {
            statement.executeUpdate(
                    "CREATE TABLE IF NOT EXISTS orders ("
                            + "id SERIAL PRIMARY KEY, "
                            + "user_id INTEGER NOT NULL REFERENCES users(id) ON DELETE CASCADE, "
                            + "clothing_id INTEGER NOT NULL REFERENCES clothing(id) ON DELETE CASCADE, "
                            + "quantity INTEGER NOT NULL CHECK (quantity > 0), "
                            + "total_price NUMERIC(10, 2) NOT NULL CHECK (total_price >= 0), "
                            + "order_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP"
                            + ")");

            statement.executeUpdate(
                    "DO $$ "
                            + "BEGIN "
                            + "IF EXISTS (SELECT 1 FROM information_schema.columns "
                            + "WHERE table_name = 'orders' AND column_name = 'product_id') "
                            + "AND NOT EXISTS (SELECT 1 FROM information_schema.columns "
                            + "WHERE table_name = 'orders' AND column_name = 'clothing_id') THEN "
                            + "ALTER TABLE orders RENAME COLUMN product_id TO clothing_id; "
                            + "END IF; "
                            + "END $$;");

            statement.executeUpdate(
                    "DO $$ "
                            + "BEGIN "
                            + "IF EXISTS (SELECT 1 FROM information_schema.columns "
                            + "WHERE table_name = 'orders' AND column_name = 'created_at') "
                            + "AND NOT EXISTS (SELECT 1 FROM information_schema.columns "
                            + "WHERE table_name = 'orders' AND column_name = 'order_date') THEN "
                            + "ALTER TABLE orders RENAME COLUMN created_at TO order_date; "
                            + "END IF; "
                            + "END $$;");

            statement.executeUpdate(
                    "ALTER TABLE IF EXISTS orders "
                            + "ADD COLUMN IF NOT EXISTS clothing_id INTEGER REFERENCES clothing(id) ON DELETE CASCADE");

            statement.executeUpdate(
                    "ALTER TABLE IF EXISTS orders "
                            + "ADD COLUMN IF NOT EXISTS order_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP");
        } catch (SQLException exception) {
            throw new IllegalStateException("Nao foi possivel preparar a tabela de compras.", exception);
        }
    }
}
