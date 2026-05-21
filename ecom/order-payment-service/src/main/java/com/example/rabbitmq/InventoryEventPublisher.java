package com.example.rabbitmq;

import com.example.dto.InventoryEvent;
import io.micronaut.rabbitmq.annotation.Binding;
import io.micronaut.rabbitmq.annotation.RabbitClient;

@RabbitClient("store.exchange")
public interface InventoryEventPublisher {

    @Binding("inventory.decrease")
    void decreaseStock(InventoryEvent event);
}