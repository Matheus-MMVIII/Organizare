package com.organizare.model;

import java.time.LocalDateTime;

public class Buy {
    private final int id;
    private final int userId;
    private final int clothingId;
    private final int quantity;
    private final double totalPrice;
    private final LocalDateTime orderDate;
    private final String userName;
    private final String clothingName;

    public Buy(int id, int userId, int clothingId, int quantity, double totalPrice) {
        this(id, userId, clothingId, quantity, totalPrice, LocalDateTime.now(), null, null);
    }

    public Buy(int id, int userId, int clothingId, int quantity, double totalPrice, LocalDateTime orderDate) {
        this(id, userId, clothingId, quantity, totalPrice, orderDate, null, null);
    }

    public Buy(int id, int userId, int clothingId, int quantity, double totalPrice, LocalDateTime orderDate,
            String userName, String clothingName) {
        this.id = id;
        this.userId = userId;
        this.clothingId = clothingId;
        this.quantity = quantity;
        this.totalPrice = totalPrice;
        this.orderDate = orderDate == null ? LocalDateTime.now() : orderDate;
        this.userName = userName;
        this.clothingName = clothingName;
    }

    public int getId() {
        return id;
    }

    public int getUserId() {
        return userId;
    }

    public int getClothingId() {
        return clothingId;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public int getQuantity() {
        return quantity;
    }

    public LocalDateTime getOrderDate() {
        return orderDate;
    }

    public String getUserName() {
        return userName;
    }

    public String getClothingName() {
        return clothingName;
    }
}
