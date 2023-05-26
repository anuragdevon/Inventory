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
        // Perform any additional operations required for item creation
    }

    public void updateItem(String name, int quantity, double price) {
        this.name = name;
        this.quantity = quantity;
        this.price = price;
        // Perform any additional operations required for item update
    }

    public String getItem() {
        return name;
        // Perform any additional operations required for retrieving an item
    }

    public String getAllItems() {
        return name;
        // Perform any additional operations required for retrieving all items
    }

    public void deleteItem() {
        // Perform any operations required for deleting an item
    }
}

