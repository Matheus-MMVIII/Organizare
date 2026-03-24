package com.organizare.model;

public abstract class Product {
    private final int id;
    private final String name;
    private double price;
    private int stock;

    // Inicializa os dados basicos compartilhados por qualquer tipo de produto.
    protected Product(int id, String name, double price, int stock) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.stock = stock;
    }

    // Retorna o identificador unico do produto.
    public int getId() {
        return id;
    }

    // Retorna o nome do produto.
    public String getName() {
        return name;
    }

    // Retorna o preco atual do produto.
    public double getPrice() {
        return price;
    }

    // Atualiza o preco do produto.
    public void setPrice(double price) {
        this.price = price;
    }

    // Retorna a quantidade em estoque.
    public int getStock() {
        return stock;
    }

    // Atualiza a quantidade disponivel em estoque.
    public void setStock(int stock) {
        this.stock = stock;
    }
}
