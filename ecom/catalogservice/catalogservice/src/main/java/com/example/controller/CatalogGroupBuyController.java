package com.example.controller;

import com.example.grpc.InventoryStreamClient;
import io.grpc.stub.StreamObserver;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.sse.Event;
import reactor.core.publisher.Flux;

import com.shop.inventory.grpc.GroupBuyProgress;

@Controller("/api/catalog/group-buys")
public class CatalogGroupBuyController {

    // 1. Inject your new client instead of the gRPC stub
    private final InventoryStreamClient inventoryStreamClient;

    public CatalogGroupBuyController(InventoryStreamClient inventoryStreamClient) {
        this.inventoryStreamClient = inventoryStreamClient;
    }

    //Flux is used to handle stream data in Front-end
    @Get(value = "/stream/{productId}", produces = MediaType.TEXT_EVENT_STREAM)
    public Flux<Event<String>> streamGroupBuy(String productId) {

        return Flux.create(emitter -> {

            // 2. Pass the productId and the StreamObserver to your client
            inventoryStreamClient.subscribe(productId, new StreamObserver<GroupBuyProgress>() {
                @Override
                public void onNext(GroupBuyProgress progress) { // onNext runs every time rGPC sends new data from InventoryGrpcService

                    // 1. Manually build a valid JSON string
                    String jsonData = String.format(
                            "{\"productId\":\"%s\", \"currentQuantity\":%d, \"targetQuantity\":%d, \"status\":\"%s\"}",
                            progress.getProductId(),
                            progress.getCurrentQuantity(),
                            progress.getTargetQuantity(),
                            progress.getStatus()
                    );

                    // 2. Send the JSON string instead of progress.toString()
                    Event<String> sseEvent = Event.of(jsonData).name("groupBuyUpdate");

                    emitter.next(sseEvent); // send data to browser
                }

                @Override
                public void onError(Throwable t) {
                    emitter.error(t);
                }

                @Override
                public void onCompleted() {
                    emitter.complete();
                }
            });

            emitter.onDispose(() -> {
                System.out.println("SSE Client disconnected for product: " + productId);
            });
        });
    }
}
