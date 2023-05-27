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
    void expectsCorrectItemIdForNewItemCreation() {
        newItem = new Item("Test Item", 1001, 9.99, 5);
        Long itemId = 1L;
        newItem.setItemId(itemId);
        assertEquals(1, newItem.getItemId());
    }

    @Test
    void expectsToGetCorrectNameForNewItemCreation() {
        newItem = new Item("Test Item", 1001, 9.99, 5);
        assertEquals("Test Item", newItem.getName());
    }

    @Test
    void expectsToGetCorrectUserIdForNewItemCreation() {
        newItem = new Item("Test Item", 1001, 9.99, 5);
        assertEquals(1001, newItem.getUserId());
    }

    @Test
    void expectsToGetCorrectPriceForNewItemCreation() {
        newItem = new Item("Test Item", 1001, 9.99, 5);
        assertEquals(9.99, newItem.getPrice(), 0.01);
    }

    @Test
    void expectsToGetCorrectQuantityForNewItemCreation() {
        newItem = new Item("Test Item", 1001, 9.99, 5);
        assertEquals(5, newItem.getQuantity());
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
