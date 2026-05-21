package com.example.grpc;

import com.shop.inventory.grpc.InventoryServiceGrpc;
import com.shop.inventory.grpc.StockRequest;
import com.shop.inventory.grpc.StockResponse;
import com.shop.inventory.grpc.ReserveRequest;
import com.shop.inventory.grpc.ReserveResponse;

import io.micronaut.grpc.annotation.GrpcService;
import io.grpc.stub.StreamObserver;

@GrpcService // 🔥 bắt buộc
public class InventoryServiceImpl extends InventoryServiceGrpc.InventoryServiceImplBase {

    @Override
    public void checkStock(StockRequest request,
                           StreamObserver<StockResponse> responseObserver) {

        System.out.println("CHECK STOCK: " + request.getProductId());

        StockResponse response = StockResponse.newBuilder()
                .setProductId(request.getProductId())
                .setAvailableQuantity(100) // giả lập
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void reserveStock(ReserveRequest request,
                             StreamObserver<ReserveResponse> responseObserver) {

        System.out.println("RESERVE: " + request.getProductId());

        ReserveResponse response = ReserveResponse.newBuilder()
                .setSuccess(true)
                .setMessage("Reserved OK")
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}