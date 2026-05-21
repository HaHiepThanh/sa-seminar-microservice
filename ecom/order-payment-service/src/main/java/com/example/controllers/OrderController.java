package com.example.controllers;

import com.example.dto.InventoryEvent;
import com.example.grpc.InventoryGrpcClient;
import com.example.grpc.InventoryServiceImpl;
import com.example.rabbitmq.InventoryEventPublisher;
import io.micronaut.scheduling.annotation.ExecuteOn;
import io.micronaut.scheduling.TaskExecutors;
import com.example.dto.CreateOrderRequest;
import com.example.dto.OrderEmailEvent;
import com.example.entities.Order;
import com.example.rabbitmq.OrderEventPublisher;
import com.example.services.OrderService;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.*;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.authentication.Authentication;
import io.micronaut.security.rules.SecurityRule;
import jakarta.inject.Inject;

import java.util.List;
import java.util.Optional;

@Controller("/api/orders")
@Secured(SecurityRule.IS_AUTHENTICATED)
public class OrderController {

    private final OrderService orderService;
    @Inject
    OrderEventPublisher emailPublisher;

    @Inject
    InventoryEventPublisher inventoryPublisher;

    @Inject
    InventoryGrpcClient inventoryGrpcClient;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    // ===== 1. GET ALL =====
    @Get
    public HttpResponse<List<Order>> getAllOrders() {
        return HttpResponse.ok(orderService.getAllOrders());
    }

    // ===== 2. GET BY ID =====
    @Get("/{id}")
    public HttpResponse<?> getOrderById(@PathVariable String id) {

        Optional<Order> order = orderService.getOrderById(id);

        return order
                .<HttpResponse<?>>map(HttpResponse::ok)
                .orElseGet(HttpResponse::notFound);
    }

    // ===== 3. CREATE =====
    @Post
    @ExecuteOn(TaskExecutors.BLOCKING)
    public HttpResponse<Order> createOrder(@Body CreateOrderRequest request,
            Authentication authentication) {

        String userId = (String) authentication.getAttributes().get("userId");
        String email = authentication.getName();

        for (var item : request.getItems()) {

            var stock = inventoryGrpcClient.checkStock(item.getProductId());

            if (stock.getAvailableQuantity() < item.getQuantity()) {
                throw new RuntimeException(
                        "Not enough stock for product: " + item.getProductId());
            }
        }

        Order order = orderService.createOrder(request, userId);

        emailPublisher.sendOrderCreated(
                new OrderEmailEvent(
                        email,
                        email,
                        order.getId(),
                        order.getTotalPrice()));

        inventoryPublisher.decreaseStock(
                new InventoryEvent(
                        order.getId(),
                        request.getItems().stream()
                                .map(i -> new InventoryEvent.Item(
                                        i.getProductId(),
                                        i.getQuantity()))
                                .toList()));

        return HttpResponse.created(order);
    }

    // ===== 4. UPDATE =====
    @Put("/{id}")
    public HttpResponse<?> updateOrder(@PathVariable String id,
            @Body Order updatedOrder) {
        try {
            Order updated = orderService.updateOrder(id, updatedOrder);
            return HttpResponse.ok(updated);
        } catch (Exception e) {
            return HttpResponse.notFound();
        }
    }

    // ===== 5. DELETE =====
    @Delete("/{id}")
    public HttpResponse<?> deleteOrder(@PathVariable String id) {
        try {
            orderService.deleteOrder(id);
            return HttpResponse.ok("Deleted successfully");
        } catch (Exception e) {
            return HttpResponse.badRequest("Delete failed: " + e.getMessage());
        }
    }
}