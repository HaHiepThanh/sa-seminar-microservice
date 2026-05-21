package service;

import com.shop.inventory.grpc.*;
import io.grpc.stub.ServerCallStreamObserver;
import io.grpc.stub.StreamObserver;
import jakarta.inject.Singleton;
import jakarta.transaction.Transactional;

import entity.Inventory;
import entity.GroupBuy;
import repository.InventoryRepository;
import repository.GroupBuyRepository;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Singleton
public class InventoryGrpcService extends InventoryServiceGrpc.InventoryServiceImplBase {

    private final InventoryRepository inventoryRepository;
    private final GroupBuyRepository groupBuyRepository;

    // active streaming clients
    private final List<ServerCallStreamObserver<GroupBuyProgress>> subscribers =
            new CopyOnWriteArrayList<>();

    public InventoryGrpcService(InventoryRepository inventoryRepository,
                                GroupBuyRepository groupBuyRepository) {
        this.inventoryRepository = inventoryRepository;
        this.groupBuyRepository = groupBuyRepository;
    }

    // ==================================================
    // 1. CHECK STOCK (UNARY - DB)
    // ==================================================
    @Override
    public void checkStock(StockRequest request,
                           StreamObserver<StockResponse> responseObserver) {

        Inventory inv = inventoryRepository.findById(request.getProductId())
                .orElseThrow();

        StockResponse response = StockResponse.newBuilder()
                .setProductId(inv.getProductId())
                .setAvailableQuantity(inv.getQuantity())
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    // ==================================================
    // 2. RESERVE STOCK (UNARY - DB + EVENT TRIGGER)
    // ==================================================
    @Override
    @Transactional
    public void reserveStock(ReserveRequest request,
                             StreamObserver<ReserveResponse> responseObserver) {

        Inventory inv = inventoryRepository.findById(request.getProductId())
                .orElseThrow();

        if (inv.getQuantity() < request.getAmount()) {
            responseObserver.onNext(
                    ReserveResponse.newBuilder()
                            .setSuccess(false)
                            .setMessage("Not enough stock")
                            .build()
            );
            responseObserver.onCompleted();
            return;
        }

        inv.setQuantity(inv.getQuantity() - request.getAmount());
        inventoryRepository.update(inv);

        // update group buy
        groupBuyRepository.findByProductIdAndStatus(inv.getProductId(), "OPEN")
                .ifPresent(gb -> {
                    gb.setCurrentQuantity(gb.getCurrentQuantity() + request.getAmount());

                    if (gb.getCurrentQuantity() >= gb.getTargetQuantity()) {
                        gb.setStatus("SUCCESS");
                    }

                    groupBuyRepository.update(gb);

                    // PUSH STREAM EVENT
                    pushGroupBuyUpdate(gb);
                });

        responseObserver.onNext(
                ReserveResponse.newBuilder()
                        .setSuccess(true)
                        .setMessage("Reserved " + request.getAmount())
                        .build()
        );

        responseObserver.onCompleted();
    }

    // ==================================================
    // 3. STREAMING SUBSCRIBE
    // ==================================================
    @Override
    public void subscribeGroupBuy(GroupBuyRequest request,
                                  StreamObserver<GroupBuyProgress> responseObserver) {

        ServerCallStreamObserver<GroupBuyProgress> obs =
                (ServerCallStreamObserver<GroupBuyProgress>) responseObserver;

        subscribers.add(obs);

        obs.setOnCancelHandler(() -> subscribers.remove(obs));

        // send initial state from DB
        groupBuyRepository.findByProductIdAndStatus(request.getProductId(), "OPEN")
                .ifPresent(gb -> obs.onNext(toProgress(gb)));
    }

    // ==================================================
    // 4. PUSH EVENT TO ALL CLIENTS
    // ==================================================
    public void pushGroupBuyUpdate(GroupBuy gb) {

        GroupBuyProgress event = toProgress(gb);

        for (ServerCallStreamObserver<GroupBuyProgress> sub : subscribers) {
            sub.onNext(event); //send to channel (in CatalogService)
        }
    }

    // ==================================================
    // 5. MAPPER
    // ==================================================
    private GroupBuyProgress toProgress(GroupBuy gb) {
        return GroupBuyProgress.newBuilder()
                .setProductId(gb.getProductId())
                .setCurrentQuantity(gb.getCurrentQuantity())
                .setTargetQuantity(gb.getTargetQuantity())
                .setStatus(gb.getStatus())
                .build();
    }
}