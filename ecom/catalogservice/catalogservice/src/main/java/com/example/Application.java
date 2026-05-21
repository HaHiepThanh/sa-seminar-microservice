package com.example;

import com.example.grpc.InventoryStreamClient;
import com.shop.inventory.grpc.GroupBuyProgress;
import io.grpc.stub.StreamObserver;
import io.micronaut.runtime.Micronaut;
import io.swagger.v3.oas.annotations.*;
import io.swagger.v3.oas.annotations.info.*;

@OpenAPIDefinition(
    info = @Info(
            title = "catalogservice",
            version = "0.0"
    )
)
public class Application {

    public static void main(String[] args) throws InterruptedException {
        Micronaut.run(Application.class, args);
    }
}