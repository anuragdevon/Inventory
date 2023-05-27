package com.example.Inventory.service;

import static org.junit.jupiter.api.Assertions.*;
import com.example.Inventory.CreateItemRequest;
import com.example.Inventory.CreateItemResponse;
import com.example.Inventory.InventoryGrpc;
import com.example.Inventory.model.Item;
import com.example.Inventory.dao.ItemDao;
import io.grpc.stub.StreamObserver;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.*;

class InventoryServiceTest {
    @Mock
    private ItemDao itemDao;

    @Mock
    private StreamObserver<CreateItemResponse> responseObserver;

    private InventoryService inventoryService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        inventoryService = new InventoryService(itemDao);
    }
}