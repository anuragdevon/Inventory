package com.example.Inventory.service;
import com.example.Inventory.*;
import com.example.Inventory.InventoryGrpc;
import com.example.Inventory.model.Item;
import com.example.Inventory.dao.ItemDao;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.stream.Collectors;

@GrpcService
public class InventoryService extends InventoryGrpc.InventoryImplBase {
    private final ItemDao itemDao;

    @Autowired
    public InventoryService(ItemDao itemDao) {
        this.itemDao = itemDao;
    }

    @Override
    public void createItem(CreateItemRequest request, StreamObserver<CreateItemResponse> responseObserver) {
        String name = request.getName();
        int quantity = request.getQuantity();
        double price = request.getPrice();
        long userId = request.getUserId();
        try {
            Item item = new Item(name, userId, price, quantity);

            Item savedItem = itemDao.save(item);

            CreateItemResponse response = CreateItemResponse.newBuilder()
                    .setStatus(201)
                    .setId((int) savedItem.getItemId())
                    .build();

            responseObserver.onNext(response);
            responseObserver.onCompleted();
        } catch (Exception e) {
            CreateItemResponse response = CreateItemResponse.newBuilder()
                    .setStatus(500)
                    .setError("Internal server error")
                    .build();

            responseObserver.onNext(response);
            responseObserver.onCompleted();
        }
    }

    @Override
    public void getItem(GetItemRequest request, StreamObserver<GetItemResponse> responseObserver) {
        int itemId = request.getId();

        try {
            Item item = itemDao.findById((long) itemId).orElse(null);

            if (item != null) {
                GetItemData itemData = GetItemData.newBuilder()
                        .setId((int) item.getItemId())
                        .setName(item.getName())
                        .setQuantity(item.getQuantity())
                        .setPrice(item.getPrice())
                        .build();

                GetItemResponse response = GetItemResponse.newBuilder()
                        .setStatus(200)
                        .setData(itemData)
                        .build();

                responseObserver.onNext(response);
            } else {
                GetItemResponse response = GetItemResponse.newBuilder()
                        .setStatus(404)
                        .setError("Item not found")
                        .build();

                responseObserver.onNext(response);
            }

            responseObserver.onCompleted();
        } catch (Exception e) {
            GetItemResponse response = GetItemResponse.newBuilder()
                    .setStatus(500)
                    .setError("Internal server error")
                    .build();

            responseObserver.onNext(response);
            responseObserver.onCompleted();
        }
    }

    @Override
    public void getAllItems(GetAllItemsRequest request, StreamObserver<GetAllItemsResponse> responseObserver) {
        try {
            List<Item> items = itemDao.findAll();

            List<GetItemData> itemDataList = items.stream()
                    .map(item -> GetItemData.newBuilder()
                            .setId((int) item.getItemId())
                            .setName(item.getName())
                            .setQuantity(item.getQuantity())
                            .setPrice(item.getPrice())
                            .build())
                    .collect(Collectors.toList());

            GetAllItemsResponse response = GetAllItemsResponse.newBuilder()
                    .setStatus(200)
                    .addAllData(itemDataList)
                    .build();

            responseObserver.onNext(response);
            responseObserver.onCompleted();
        } catch (Exception e) {
            GetAllItemsResponse response = GetAllItemsResponse.newBuilder()
                    .setStatus(500)
                    .setError("Internal server error")
                    .build();

            responseObserver.onNext(response);
            responseObserver.onCompleted();
        }
    }

    @Override
    public void updateItem(UpdateItemRequest request, StreamObserver<UpdateItemResponse> responseObserver) {
        try {
            int itemId = request.getId();
            String name = request.getName();
            int quantity = request.getQuantity();
            double price = request.getPrice();

            Item item = itemDao.findById((long) itemId)
                    .orElse(null);

            if (item != null) {
                item.setName(name);
                item.setQuantity(quantity);
                item.setPrice(price);

                Item updatedItem = itemDao.save(item);

                GetItemData itemData = GetItemData.newBuilder()
                        .setId((int) updatedItem.getItemId())
                        .setName(updatedItem.getName())
                        .setQuantity(updatedItem.getQuantity())
                        .setPrice(updatedItem.getPrice())
                        .build();

                UpdateItemResponse response = UpdateItemResponse.newBuilder()
                        .setStatus(200)
                        .setData(itemData)
                        .build();

                responseObserver.onNext(response);
            } else {
                UpdateItemResponse response = UpdateItemResponse.newBuilder()
                        .setStatus(404)
                        .setError("Item not found")
                        .build();

                responseObserver.onNext(response);
            }
        } catch (Exception e) {
            UpdateItemResponse response = UpdateItemResponse.newBuilder()
                    .setStatus(500)
                    .setError("Internal server error")
                    .build();

            responseObserver.onNext(response);
        }

        responseObserver.onCompleted();
    }
}
