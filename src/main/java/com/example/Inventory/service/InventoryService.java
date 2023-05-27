package com.example.Inventory.service;

import com.example.Inventory.CreateItemRequest;
import com.example.Inventory.CreateItemResponse;
import com.example.Inventory.InventoryGrpc;
import com.example.Inventory.model.Item;
import com.example.Inventory.dao.ItemDao;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.beans.factory.annotation.Autowired;

@GrpcService
public class InventoryService extends InventoryGrpc.InventoryImplBase {
    private ItemDao itemDao;

    @Autowired
    public InventoryService(ItemDao itemDao) {
        this.itemDao = itemDao;
    }

    @Override
    public void createItem(CreateItemRequest request, StreamObserver<CreateItemResponse> responseObserver) {

    }
}
