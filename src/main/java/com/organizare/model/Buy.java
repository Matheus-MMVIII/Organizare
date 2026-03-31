package com.organizare.model;

import java.time.LocalDateTime;

public class Buy {
    private final User user;
    private final Product product;
    public final int quantity;
    private final LocalDateTime createdAt;

    public Buy(User user, Product product, int quantity) {
        this(user, product, quantity, LocalDateTime.now());
    }

    public Buy(User user, Product product, int quantity, LocalDateTime createdAt) {
        this.user = user;
        this.product = product;
        this.quantity = quantity;
        this.createdAt = createdAt == null ? LocalDateTime.now() : createdAt;
    }

    public User getUser() {
        return user;
    }

    public int getUserId() {
        return user.getId();
    }

    public Product getProduct() {
        return product;
    }

    public int getProductId() {
        return product.getId();
    }

    public Double getProductPrice() {
        return product.getPrice();
    }

    public int getQuantity() {
        return quantity;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
