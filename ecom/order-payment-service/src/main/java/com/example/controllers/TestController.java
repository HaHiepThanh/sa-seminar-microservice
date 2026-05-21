package com.example.controllers;

import com.example.grpc.InventoryGrpcClient;
import com.shop.inventory.grpc.StockResponse;
import io.micronaut.http.annotation.*;

@Controller("/test")
public class TestController {

    private final InventoryGrpcClient client;

    public TestController(InventoryGrpcClient client) {
        this.client = client;
    }

    @Get("/stock/{productId}")
    public int checkStock(@PathVariable String productId) {
        return client.checkStock(productId).getAvailableQuantity();
    }
}
