package com.example.grpc;

import com.shop.inventory.grpc.*;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import jakarta.inject.Singleton;

@Singleton
public class InventoryStreamClient {

    private final InventoryServiceGrpc.InventoryServiceStub asyncStub;

    public InventoryStreamClient() {
        String host = System.getenv("INVENTORY_GRPC_HOST");
        if (host == null || host.isEmpty()) {
            host = "localhost";
        }
        ManagedChannel channel = ManagedChannelBuilder
                .forAddress(host, 50051)
                .usePlaintext()
                .build();

        this.asyncStub = InventoryServiceGrpc.newStub(channel);
    }

    public void subscribe(String productId,
                          StreamObserver<GroupBuyProgress> observer) {

        asyncStub.subscribeGroupBuy(
                GroupBuyRequest.newBuilder()
                        .setProductId(productId)
                        .build(),
                observer
        );
    }
}

