package com.example;

import io.micronaut.rabbitmq.annotation.Queue;
import io.micronaut.rabbitmq.annotation.RabbitListener;
import jakarta.inject.Inject;

@RabbitListener
public class OrderEmailListener {

    @Inject
    private EmailService emailService;

    @Queue("order-email-queue")
    public void receive(OrderEmailEvent event) {

        System.out.println("📩 Received order event: " + event.orderId());

        emailService.sendOrderConfirmationEmail(
                event.email(),
                event.customerName(),
                event.orderId(),
                event.totalAmount()
        );
    }
}