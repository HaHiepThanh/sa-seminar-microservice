package rabbitmq;

import dto.request.InventoryEvent;
import io.micronaut.rabbitmq.annotation.Queue;
import io.micronaut.rabbitmq.annotation.RabbitListener;
import jakarta.inject.Inject;
import service.GroupBuyService;
import service.InventoryService;

@RabbitListener
public class InventoryListener {

    @Inject
    InventoryService inventoryService;

    @Inject
    GroupBuyService groupBuyService;

    @Queue("inventory-queue")
    public void handle(InventoryEvent event) {

        System.out.println("📦 Processing order: " + event.orderId());

        for (InventoryEvent.Item item : event.items()) {

            // 1. decrease stock
            inventoryService.decreaseStock(
                    item.productId(),
                    item.quantity()
            );

            // 2. update group buy if exists
            groupBuyService.increaseIfOpen(
                    item.productId(),
                    item.quantity()
            );
        }
    }
}