package com.organizare.model;

import java.time.LocalDateTime;

public class Buy {
    private final int id;
    private final int user_id;
    private final int product_id;
    private final int quantity;
    private final Double total_price;
    private final LocalDateTime createdAt;

    public Buy(int id, int user_id, int product_id, int quantity, Double total_price) {
        this(id, user_id, product_id, quantity, total_price, LocalDateTime.now());
    }

    public Buy(int id, int user_id, int product_id, int quantity, Double total_price, LocalDateTime createdAt) {
        this.id = id;
        this.user_id = user_id;
        this.product_id = product_id;
        this.quantity = quantity;
        this.total_price = total_price;
        this.createdAt = createdAt == null ? LocalDateTime.now() : createdAt;
    }

    public int getId() {
        return id;
    }

    public int getUserId() {
        return user_id;
    }

    public int getProductId() {
        return product_id;
    }

    public Double getProductPrice() {
        return total_price;
    }

    public int getQuantity() {
        return quantity;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
