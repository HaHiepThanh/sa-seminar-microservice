package com.example.rabbitmq;

import com.example.dto.OrderEmailEvent;
import io.micronaut.rabbitmq.annotation.Binding;
import io.micronaut.rabbitmq.annotation.RabbitClient;

@RabbitClient("store.exchange")
public interface OrderEventPublisher {

    @Binding("order.created")
    void sendOrderCreated(OrderEmailEvent event);
}