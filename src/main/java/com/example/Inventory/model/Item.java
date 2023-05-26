package com.example.Inventory.model;

public class Item {
    private long itemId;
    private String name;
    private long userId;
    private double price;
    private int quantity;

    public Item(long itemId, String name, long userId, double price, int quantity) {
        if (quantity < 0) {
            throw new IllegalArgumentException("item quantity cannot be negative");
        }

        if (price < 0) {
            throw new IllegalArgumentException("item price cannot be negative");
        }
        this.itemId = itemId;
        this.name = name;
        this.userId = userId;
        this.price = price;
        this.quantity = quantity;
    }

    public long getItemId() {
        return itemId;
    }

    public String getName() {
        return name;
    }

    public long getUserId() {
        return userId;
    }

    public double getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }
}
