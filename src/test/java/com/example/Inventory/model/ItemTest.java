package com.example.Inventory.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ItemTest {
    private Item newItem;

    @Test
    public void expectsNotToThrowErrorForNewItemCreation() {
        assertDoesNotThrow(() -> {new Item("Test Item", 1001, 9.99, 5);});
    }

    @Test
    void expectsToThrowErrorForNegativeQuantityForNewItemCreation() {
        assertThrows(IllegalArgumentException.class, () -> new Item("Negative Quantity Item", 1002, 9.99, -5));
    }

    @Test
    void expectsToThrowErrorForNegativePriceForNewItemCreation() {
        assertThrows(IllegalArgumentException.class, () -> new Item("Negative Price Item", 1003, -9.99, 10));
    }
}
