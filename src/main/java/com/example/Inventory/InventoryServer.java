
package com.example.Inventory;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import com.example.Inventory.service.InventoryService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class InventoryServer {

    private static final int PORT = 9090;

    @Autowired
    private InventoryService inventoryService;

    public static void main(String[] args) throws Exception {
        ConfigurableApplicationContext context = SpringApplication.run(InventoryServer.class, args);
        InventoryServer inventoryServer = context.getBean(InventoryServer.class);
        inventoryServer.startGrpcServer();
    }

    private void startGrpcServer() throws Exception {
        // Start the gRPC server
        Server server = ServerBuilder.forPort(PORT)
                .addService(inventoryService)
                .build();

        server.start();

        System.out.println("Inventory gRPC server started on port " + PORT);

        // Shutdown the server gracefully
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("Shutting down Inventory gRPC server...");
            server.shutdown();
            System.out.println("Inventory gRPC server shut down successfully.");
        }));

        server.awaitTermination();
    }
}
