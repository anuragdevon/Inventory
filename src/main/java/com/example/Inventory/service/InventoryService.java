package com.example.Inventory.service;

import com.example.Inventory.InventoryGrpc;
import net.devh.boot.grpc.server.service.GrpcService;

@GrpcService
public class InventoryService extends InventoryGrpc.InventoryImplBase {
}
