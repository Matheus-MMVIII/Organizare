package com.organizare.model;

public class Clothing extends Product {
    private String size;
    private String color;

    // Inicializa uma peca de roupa com os dados herdados de produto e seus atributos especificos.
    public Clothing(int id, String name, double price, int stock, String size, String color) {
        super(id, name, price, stock);
        this.size = size;
        this.color = color;
    }

    // Retorna o tamanho da roupa.
    public String getSize() {
        return size;
    }

    // Atualiza o tamanho da roupa.
    public void setSize(String size) {
        this.size = size;
    }

    // Retorna a cor da roupa.
    public String getColor() {
        return color;
    }

    // Atualiza a cor da roupa.
    public void setColor(String color) {
        this.color = color;
    }
}
