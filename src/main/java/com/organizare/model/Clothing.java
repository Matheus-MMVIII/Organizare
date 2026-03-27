package com.organizare.model;

public class Clothing extends Product {
    private Size size;
    private String color;

    public Clothing(int id, String name, double price, int stock, Size size, String color) {
        super(id, name, price, stock);
        this.size = size;
        this.color = color;
    }

    public Size getSize() {
        return size;
    }

    public String getColor() {
        return color;
    }

}
