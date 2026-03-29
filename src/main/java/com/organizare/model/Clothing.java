package com.organizare.model;

public class Clothing extends Product {
    private Size size;
    private String color;

    public Clothing(int id, String name, double price, int stock, String size, String color) {
        super(id, name, price, stock);
        this.size = Size.valueOf(size);
        this.color = color;
    }

    public Size getSize() {
        return size;
    }

    public String getSizeAsString() {
        return size.name();
    }

    public String getColor() {
        return color;
    }

}
