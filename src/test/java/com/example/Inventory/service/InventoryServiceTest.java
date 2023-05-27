package com.example.Inventory.service;

import static org.junit.jupiter.api.Assertions.*;
import com.example.Inventory.*;
import com.example.Inventory.model.Item;
import com.example.Inventory.dao.ItemDao;
import io.grpc.stub.StreamObserver;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class InventoryServiceTest {
    @Mock
    private ItemDao itemDao;

    @Mock
    private StreamObserver<CreateItemResponse> createItemResponseObserver;

    @Mock
    private StreamObserver<GetItemResponse> getItemResponseObserver;

    @Captor
    private ArgumentCaptor<GetItemResponse> getItemResponseCaptor;

    private InventoryService inventoryService;

    @BeforeEach
    void setUp() {
        inventoryService = new InventoryService(itemDao);
    }

    @Nested
    public class CreateItemTest {
        @Test
        void expectsToReturnStatus201CreatedResponseForValidRequest() {
            CreateItemRequest request = CreateItemRequest.newBuilder()
                    .setName("Test Item")
                    .setQuantity(5)
                    .setUserId(1001)
                    .setPrice(80.0)
                    .build();

            Item savedItem = new Item("Test Item", 1001, 80.0, 5);
            savedItem.setItemId(1L);

            when(itemDao.save(any(Item.class))).thenReturn(savedItem);

            inventoryService.createItem(request, createItemResponseObserver);

            CreateItemResponse expectedResponse = CreateItemResponse.newBuilder()
                    .setStatus(201)
                    .setId((int) savedItem.getItemId())
                    .build();
            verify(createItemResponseObserver).onNext(expectedResponse);
            verify(createItemResponseObserver).onCompleted();
        }

        @Test
        void expectsToReturnStatus500InternalServerErrorForDatabaseError() {
            CreateItemRequest request = CreateItemRequest.newBuilder()
                    .setName("Test Item")
                    .setQuantity(5)
                    .setUserId(1001)
                    .setPrice(80.0)
                    .build();

            when(itemDao.save(any(Item.class))).thenThrow(new RuntimeException("Database error"));

            inventoryService.createItem(request, createItemResponseObserver);

            CreateItemResponse expectedResponse = CreateItemResponse.newBuilder()
                    .setStatus(500)
                    .setError("Internal server error")
                    .build();
            verify(createItemResponseObserver).onNext(expectedResponse);
            verify(createItemResponseObserver).onCompleted();
        }
    }

    @Nested
    public class GetItemTest {
        @Test
        void expectsToReturnStatus200OKAndItemForValidItemIdInRequest() {
            int itemId = 123;
            Item item = new Item("Test Item", 1001, 9.99, 5);
            item.setItemId(123L);

            when(itemDao.findById((long) itemId)).thenReturn(Optional.of(item));

            GetItemRequest request = GetItemRequest.newBuilder()
                    .setId(itemId)
                    .build();
            InventoryService inventoryService = new InventoryService(itemDao);
            inventoryService.getItem(request, getItemResponseObserver);

            verify(getItemResponseObserver).onNext(getItemResponseCaptor.capture());
            verify(getItemResponseObserver).onCompleted();

            GetItemResponse response = getItemResponseCaptor.getValue();
            assertEquals(200, response.getStatus());

            GetItemData itemData = response.getData();
            assertEquals(item.getItemId(), itemData.getId());
            assertEquals(item.getName(), itemData.getName());
            assertEquals(item.getQuantity(), itemData.getQuantity());
            assertEquals(item.getPrice(), itemData.getPrice());
        }

        @Test
        void expectsToReturnStatus404NotFoundForItemIdNotInInventory() {
            int itemId = 123;

            when(itemDao.findById((long) itemId)).thenReturn(Optional.empty());

            GetItemRequest request = GetItemRequest.newBuilder().setId(itemId).build();
            InventoryService inventoryService = new InventoryService(itemDao);
            inventoryService.getItem(request, getItemResponseObserver);

            verify(getItemResponseObserver).onNext(getItemResponseCaptor.capture());
            verify(getItemResponseObserver).onCompleted();

            GetItemResponse response = getItemResponseCaptor.getValue();
            assertEquals(404, response.getStatus());
            assertEquals("Item not found", response.getError());
        }

        @Test
        void expectsToReturnStatus500InternalServerErrorForAnException() {
            int itemId = 123;

            when(itemDao.findById((long) itemId)).thenThrow(new RuntimeException("Database error"));

            GetItemRequest request = GetItemRequest.newBuilder().setId(itemId).build();
            InventoryService inventoryService = new InventoryService(itemDao);
            inventoryService.getItem(request, getItemResponseObserver);

            verify(getItemResponseObserver).onNext(getItemResponseCaptor.capture());
            verify(getItemResponseObserver).onCompleted();

            GetItemResponse response = getItemResponseCaptor.getValue();
            assertEquals(500, response.getStatus());
            assertEquals("Internal server error", response.getError());
        }
    }
}
