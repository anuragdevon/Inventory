package com.example.Inventory.model;

public class Inventory {
    private int productId;
    private String name;
    private int quantity;
    private double price;

    public Inventory(int productId, String name, int quantity, double price) {
        this.productId = productId;
        this.name = name;
        this.quantity = quantity;
        this.price = price;
    }

    public void createItem(int productId, String name, int quantity, double price) {
        this.productId = productId;
        this.name = name;
        this.quantity = quantity;
        this.price = price;
    }

    public void updateItem(String name, int quantity, double price) {
        this.name = name;
        this.quantity = quantity;
        this.price = price;
    }

    public String getItem() {
        return name;
    }

    public String getAllItems() {
        return name;
    }

    public void deleteItem() {
    }
}

