package com.example.Inventory.model;

import javax.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "item_id")
    private Long itemId;

    @Column(name = "name")
    private String name;

    @Column(name = "user_id")
    private long userId;

    @Column(name = "price")
    private double price;

    @Column(name = "quantity")
    private int quantity;

    public Item() {}

    public Item(String name, long userId, double price, int quantity) {
        if (quantity < 0) {
            throw new IllegalArgumentException("Item quantity cannot be negative");
        }

        if (price < 0.0) {
            throw new IllegalArgumentException("Item price cannot be negative");
        }
        this.name = name;
        this.userId = userId;
        this.price = price;
        this.quantity = quantity;
    }
}
