package com.example.Inventory.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;

@Entity
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long itemId;
    private String name;
    private long userId;
    private double price;
    private int quantity;

    public Item(String name, long userId, double price, int quantity) {
        if (quantity < 0) {
            throw new IllegalArgumentException("item quantity cannot be negative");
        }

        if (price < 0.0) {
            throw new IllegalArgumentException("item price cannot be negative");
        }
        this.name = name;
        this.userId = userId;
        this.price = price;
        this.quantity = quantity;
    }

    public void setItemId(Long itemId) {
        this.itemId = itemId;
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
