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

import java.util.Arrays;
import java.util.List;
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

    @Mock
    private StreamObserver<GetAllItemsResponse> getAllItemsResponseObserver;

    @Mock
    private StreamObserver<UpdateItemResponse> updateItemResponseObserver;

    @Mock
    private StreamObserver<DeleteItemResponse> deleteItemResponseObserver;

    @Mock
    private StreamObserver<DecreaseItemQuantityResponse> decreaseItemQuantityResponseObserver;

    @Captor
    private ArgumentCaptor<GetItemResponse> getItemResponseCaptor;

    @Captor
    private ArgumentCaptor<GetAllItemsResponse> getAllItemsResponseCaptor;

    @Captor
    private ArgumentCaptor<UpdateItemResponse> updateItemResponseCaptor;

    @Captor
    private ArgumentCaptor<DeleteItemResponse> deleteItemResponseCaptor;

    @Captor
    private ArgumentCaptor<DecreaseItemQuantityResponse> decreaseItemQuantityCaptor;

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
                    .setId(Math.toIntExact(savedItem.getItemId()))
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
        void expectsToReturnStatus404NotFoundForItemNotInInventory() {
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

    @Nested
    public class GetAllItemsMethodTest {
        @Test
        void expectsToReturnStatus200OKAndAllItems() {
            List<Item> items = Arrays.asList(
                    new Item("Item 1", 1001, 9.99, 5),
                    new Item("Item 2", 1002, 14.99, 10),
                    new Item("Item 3", 1003, 19.99, 7)
            );
            items.get(0).setItemId(1L);
            items.get(1).setItemId(2L);
            items.get(2).setItemId(3L);

            when(itemDao.findAll()).thenReturn(items);

            GetAllItemsRequest request = GetAllItemsRequest.newBuilder().build();

            inventoryService.getAllItems(request, getAllItemsResponseObserver);

            verify(getAllItemsResponseObserver).onNext(getAllItemsResponseCaptor.capture());
            verify(getAllItemsResponseObserver).onCompleted();

            GetAllItemsResponse response = getAllItemsResponseCaptor.getValue();
            assertEquals(200, response.getStatus());

            List<GetItemData> itemDataList = response.getDataList();
            assertEquals(items.size(), itemDataList.size());

            for (int i = 0; i < items.size(); i++) {
                Item item = items.get(i);
                GetItemData itemData = itemDataList.get(i);

                assertEquals(item.getItemId(), itemData.getId());
                assertEquals(item.getName(), itemData.getName());
                assertEquals(item.getQuantity(), itemData.getQuantity());
                assertEquals(item.getPrice(), itemData.getPrice());
            }
        }

        @Test
        void expectsToReturnStatus500InternalServerErrorIfExceptionOccurs() {
            when(itemDao.findAll()).thenThrow(new RuntimeException("Database error"));

            GetAllItemsRequest request = GetAllItemsRequest.newBuilder().build();

            inventoryService.getAllItems(request, getAllItemsResponseObserver);

            verify(getAllItemsResponseObserver).onNext(getAllItemsResponseCaptor.capture());
            verify(getAllItemsResponseObserver).onCompleted();

            GetAllItemsResponse response = getAllItemsResponseCaptor.getValue();
            assertEquals(500, response.getStatus());
            assertEquals("Internal server error", response.getError());
        }
    }

    @Nested
    public class UpdateItemTest {
        @Test
        void expectsToReturnStatus200OKAndUpdatedItemForValidRequest() {
            int itemId = 123;
            String newName = "Updated Item";
            int newQuantity = 8;
            double newPrice = 12.99;

            Item item = new Item("Test Item", 1001, 9.99, 5);
            item.setItemId((long) itemId);

            when(itemDao.findById((long) itemId)).thenReturn(Optional.of(item));
            when(itemDao.save(item)).thenReturn(item);

            UpdateItemRequest request = UpdateItemRequest.newBuilder()
                    .setId(itemId)
                    .setName(newName)
                    .setQuantity(newQuantity)
                    .setPrice(newPrice)
                    .build();

            InventoryService inventoryService = new InventoryService(itemDao);
            inventoryService.updateItem(request, updateItemResponseObserver);

            verify(updateItemResponseObserver).onNext(updateItemResponseCaptor.capture());
            verify(updateItemResponseObserver).onCompleted();

            UpdateItemResponse response = updateItemResponseCaptor.getValue();
            assertEquals(200, response.getStatus());

            GetItemData itemData = response.getData();
            assertEquals(itemId, itemData.getId());
            assertEquals(newName, itemData.getName());
            assertEquals(newQuantity, itemData.getQuantity());
            assertEquals(newPrice, itemData.getPrice());
        }

        @Test
        void expectsToReturnStatus404NotFoundForForItemNotInInventory() {
            int itemId = 123;
            String newName = "Updated Item";
            int newQuantity = 8;
            double newPrice = 12.99;

            when(itemDao.findById((long) itemId)).thenReturn(Optional.empty());

            UpdateItemRequest request = UpdateItemRequest.newBuilder()
                    .setId(itemId)
                    .setName(newName)
                    .setQuantity(newQuantity)
                    .setPrice(newPrice)
                    .build();

            InventoryService inventoryService = new InventoryService(itemDao);
            inventoryService.updateItem(request, updateItemResponseObserver);

            verify(updateItemResponseObserver).onNext(updateItemResponseCaptor.capture());
            verify(updateItemResponseObserver).onCompleted();

            UpdateItemResponse response = updateItemResponseCaptor.getValue();
            assertEquals(404, response.getStatus());
            assertEquals("Item not found", response.getError());
        }

        @Test
        void expectsToReturnStatus500InternalServerErrorForAnException() {
            int itemId = 123;
            String newName = "Updated Item";
            int newQuantity = 8;
            double newPrice = 12.99;

            Item item = new Item("Test Item", 1001, 9.99, 5);
            item.setItemId((long) itemId);

            when(itemDao.findById((long) itemId)).thenReturn(Optional.of(item));
            when(itemDao.save(item)).thenThrow(new RuntimeException("Database error"));

            UpdateItemRequest request = UpdateItemRequest.newBuilder()
                    .setId(itemId)
                    .setName(newName)
                    .setQuantity(newQuantity)
                    .setPrice(newPrice)
                    .build();

            InventoryService inventoryService = new InventoryService(itemDao);
            inventoryService.updateItem(request, updateItemResponseObserver);

            verify(updateItemResponseObserver).onNext(updateItemResponseCaptor.capture());
            verify(updateItemResponseObserver).onCompleted();

            UpdateItemResponse response = updateItemResponseCaptor.getValue();
            assertEquals(500, response.getStatus());
            assertEquals("Internal server error", response.getError());
        }
    }

    @Nested
    public class DeleteItemTest {
        @Test
        void expectsToDeleteItemAndReturnStatus200ForExistingItem() {
            int itemId = 123;

            Item item = new Item("Test Item", 1001, 9.99, 5);
            item.setItemId((long) itemId);

            when(itemDao.findById((long) itemId)).thenReturn(Optional.of(item));

            DeleteItemRequest request = DeleteItemRequest.newBuilder()
                    .setId(itemId)
                    .build();

            InventoryService inventoryService = new InventoryService(itemDao);
            inventoryService.deleteItem(request, deleteItemResponseObserver);

            verify(deleteItemResponseObserver).onNext(deleteItemResponseCaptor.capture());
            verify(deleteItemResponseObserver).onCompleted();

            DeleteItemResponse response = deleteItemResponseCaptor.getValue();
            assertEquals(200, response.getStatus());

            verify(itemDao).delete(item);
        }

        @Test
        void expectsToReturnStatus404NotFoundForNonExistingItem() {
            int itemId = 123;

            when(itemDao.findById((long) itemId)).thenReturn(Optional.empty());

            DeleteItemRequest request = DeleteItemRequest.newBuilder()
                    .setId(itemId)
                    .build();

            InventoryService inventoryService = new InventoryService(itemDao);
            inventoryService.deleteItem(request, deleteItemResponseObserver);

            verify(deleteItemResponseObserver).onNext(deleteItemResponseCaptor.capture());
            verify(deleteItemResponseObserver).onCompleted();

            DeleteItemResponse response = deleteItemResponseCaptor.getValue();
            assertEquals(404, response.getStatus());
            assertEquals("Item not found", response.getError());

            verify(itemDao, never()).delete(any(Item.class));
        }

        @Test
        void expectsToReturnStatus500InternalServerErrorForDatabaseError() {
            int itemId = 123;

            when(itemDao.findById((long) itemId)).thenThrow(new RuntimeException("Database error"));

            DeleteItemRequest request = DeleteItemRequest.newBuilder()
                    .setId(itemId)
                    .build();

            InventoryService inventoryService = new InventoryService(itemDao);
            inventoryService.deleteItem(request, deleteItemResponseObserver);

            verify(deleteItemResponseObserver).onNext(deleteItemResponseCaptor.capture());
            verify(deleteItemResponseObserver).onCompleted();

            DeleteItemResponse response = deleteItemResponseCaptor.getValue();
            assertEquals(500, response.getStatus());
            assertEquals("Internal server error", response.getError());

            verify(itemDao, never()).delete(any(Item.class));
        }

        @Nested
        public class DecreaseItemQuantityTest {
            @Test
            void expectsToDecreaseItemQuantityAndReturnStatus200ForSufficientQuantity() {
                int itemId = 123;
                int currentQuantity = 10;
                int quantityToDecrease = 5;

                Item item = new Item("Test Item", 1001, 9.99, currentQuantity);
                item.setItemId((long) itemId);

                when(itemDao.findById((long) itemId)).thenReturn(Optional.of(item));

                DecreaseItemQuantityRequest request = DecreaseItemQuantityRequest.newBuilder()
                        .setId(itemId)
                        .setQuantity(quantityToDecrease)
                        .build();

                InventoryService inventoryService = new InventoryService(itemDao);
                inventoryService.decreaseItemQuantity(request, decreaseItemQuantityResponseObserver);

                verify(decreaseItemQuantityResponseObserver).onNext(decreaseItemQuantityCaptor.capture());
                verify(decreaseItemQuantityResponseObserver).onCompleted();

                DecreaseItemQuantityResponse response = decreaseItemQuantityCaptor.getValue();
                assertEquals(200, response.getStatus());

                assertEquals(currentQuantity - quantityToDecrease, item.getQuantity());
                verify(itemDao).save(item);
            }

            @Test
            void expectsToReturnStatus400InsufficientQuantityForNotEnoughQuantity() {
                int itemId = 123;
                int currentQuantity = 5;
                int quantityToDecrease = 10;

                Item item = new Item("Test Item", 1001, 9.99, currentQuantity);
                item.setItemId((long) itemId);

                when(itemDao.findById((long) itemId)).thenReturn(Optional.of(item));

                DecreaseItemQuantityRequest request = DecreaseItemQuantityRequest.newBuilder()
                        .setId(itemId)
                        .setQuantity(quantityToDecrease)
                        .build();

                InventoryService inventoryService = new InventoryService(itemDao);
                inventoryService.decreaseItemQuantity(request, decreaseItemQuantityResponseObserver);

                verify(decreaseItemQuantityResponseObserver).onNext(decreaseItemQuantityCaptor.capture());
                verify(decreaseItemQuantityResponseObserver).onCompleted();

                DecreaseItemQuantityResponse response = decreaseItemQuantityCaptor.getValue();
                assertEquals(400, response.getStatus());
                assertEquals("Insufficient quantity", response.getError());

                assertEquals(currentQuantity, item.getQuantity());
                verify(itemDao, never()).save(any(Item.class));
            }

            @Test
            void expectsToReturnStatus404NotFoundForNonExistingItem() {
                int itemId = 123;

                when(itemDao.findById((long) itemId)).thenReturn(Optional.empty());

                DecreaseItemQuantityRequest request = DecreaseItemQuantityRequest.newBuilder()
                        .setId(itemId)
                        .setQuantity(5)
                        .build();

                InventoryService inventoryService = new InventoryService(itemDao);
                inventoryService.decreaseItemQuantity(request, decreaseItemQuantityResponseObserver);

                verify(decreaseItemQuantityResponseObserver).onNext(decreaseItemQuantityCaptor.capture());
                verify(decreaseItemQuantityResponseObserver).onCompleted();

                DecreaseItemQuantityResponse response = decreaseItemQuantityCaptor.getValue();
                assertEquals(404, response.getStatus());
                assertEquals("Item not found", response.getError());

                verify(itemDao, never()).save(any(Item.class));
            }

            @Test
            void expectsToReturnStatus500InternalServerErrorForDatabaseError() {
                int itemId = 123;

                when(itemDao.findById((long) itemId)).thenThrow(new RuntimeException("Database error"));

                DecreaseItemQuantityRequest request = DecreaseItemQuantityRequest.newBuilder()
                        .setId(itemId)
                        .setQuantity(5)
                        .build();

                InventoryService inventoryService = new InventoryService(itemDao);
                inventoryService.decreaseItemQuantity(request, decreaseItemQuantityResponseObserver);

                verify(decreaseItemQuantityResponseObserver).onNext(decreaseItemQuantityCaptor.capture());
                verify(decreaseItemQuantityResponseObserver).onCompleted();

                DecreaseItemQuantityResponse response = decreaseItemQuantityCaptor.getValue();
                assertEquals(500, response.getStatus());
                assertEquals("Internal server error", response.getError());

                verify(itemDao, never()).save(any(Item.class));
            }

        }
    }
}
