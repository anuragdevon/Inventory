package com.example.Inventory.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ItemTest {
    private Item newItem;

    @Test
    public void expectsNotToThrowErrorForNewItemCreation() {
        assertDoesNotThrow(() -> {new Item(1, "Test Item", 1001, 9.99, 5);});
    }

    @Test
    void expectsCorrectItemIdForNewItemCreation() {
        newItem = new Item(1, "Test Item", 1001, 9.99, 5);
        assertEquals(1, newItem.getItemId());
    }

    @Test
    void expectsCorrectNameForNewItemCreation() {
        newItem = new Item(1, "Test Item", 1001, 9.99, 5);
        assertEquals("Test Item", newItem.getName());
    }

    @Test
    void expectsCorrectUserIdForNewItemCreation() {
        newItem = new Item(1, "Test Item", 1001, 9.99, 5);
        assertEquals(1001, newItem.getUserId());
    }

    @Test
    void expectsCorrectPriceForNewItemCreation() {
        newItem = new Item(1, "Test Item", 1001, 9.99, 5);
        assertEquals(9.99, newItem.getPrice(), 0.01);
    }

    @Test
    void expectsCorrectQuantityForNewItemCreation() {
        newItem = new Item(1, "Test Item", 1001, 9.99, 5);
        assertEquals(5, newItem.getQuantity());
    }

    @Test
    void expectsToThrowErrorForNegativeQuantityForNewItemCreation() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Item(2, "Negative Quantity Item", 1002, 9.99, -5);
        });
    }

    @Test
    void expectsToThrowErrorForNegativePriceForNewItemCreation() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Item(3, "Negative Price Item", 1003, -9.99, 10);
        });
    }
}
