package com.example.services;

import com.example.entities.*;
import com.example.repositories.*;
import com.example.dto.CreateOrderRequest;
import com.example.dto.OrderCreatedEvent;
import com.example.grpc.InventoryGrpcClient;

import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.transaction.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

@Singleton
public class OrderService {

    @Inject
    InventoryGrpcClient inventoryClient;


    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final PaymentRepository paymentRepository;

    public OrderService(OrderRepository orderRepository,
                        OrderItemRepository orderItemRepository,
                        PaymentRepository paymentRepository) {
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
        this.paymentRepository = paymentRepository;
    }

    // ================= CREATE =================
    @Transactional
    public Order createOrder(CreateOrderRequest request, String userId) {

        // ===== 1. TẠO ORDER =====
        Order order = new Order();
        order.setId(UUID.randomUUID().toString()); // hoặc "order_001"
        order.setUserId(userId);
        order.setPhone(request.getPhone());
        order.setStatus("PENDING");
        order.setAddressLine(request.getAddressLine());
        order.setCity(request.getCity());
        order.setCountry(request.getCountry());

        // ===== 2. TÍNH TOTAL =====
        BigDecimal total = BigDecimal.ZERO;

        for (var item : request.getItems()) {
            BigDecimal itemTotal = item.getUnitPrice()
                    .multiply(BigDecimal.valueOf(item.getQuantity()));
            total = total.add(itemTotal);
        }

        order.setTotalPrice(total);

        // ===== 3. SAVE ORDER =====
        Order savedOrder = orderRepository.save(order);

        // ===== 4. SAVE ORDER ITEMS =====
        for (var itemDto : request.getItems()) {

            OrderItem item = new OrderItem();
            item.setId(UUID.randomUUID().toString());
            item.setOrderId(savedOrder.getId());

            item.setProductId(itemDto.getProductId());
            item.setProductName(itemDto.getProductName()); // lấy trực tiếp từ request
            item.setUnitPrice(itemDto.getUnitPrice());
            item.setQuantity(itemDto.getQuantity());

            orderItemRepository.save(item);
        }

        // ===== 5. CREATE PAYMENT =====
        Payment payment = new Payment();
        payment.setId(UUID.randomUUID().toString());
        payment.setOrderId(savedOrder.getId());
        payment.setPaymentMethod("COD");
        payment.setStatus("SUCCESS");
        payment.setPaidAt(LocalDateTime.now());

        paymentRepository.save(payment);

        return savedOrder;
    }

    // ================= READ =================
    public List<Order> getAllOrders() {
        return (List<Order>) orderRepository.findAll();
    }

    public Optional<Order> getOrderById(String id) {
        return orderRepository.findById(id);
    }

    // ================= UPDATE =================
    @Transactional
    public Order updateOrder(String id, Order updated) {

        Order existing = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        existing.setPhone(updated.getPhone());
        existing.setCity(updated.getCity());
        existing.setCountry(updated.getCountry());
        existing.setAddressLine(updated.getAddressLine());
        existing.setStatus(updated.getStatus());

        return orderRepository.update(existing);
    }

    // ================= DELETE =================
    @Transactional
    public void deleteOrder(String id) {

        // Xóa bảng con trước (child)
        orderItemRepository.deleteByOrderId(id);
        paymentRepository.deleteByOrderId(id);

        // Sau đó mới xóa bảng cha (parent)
        orderRepository.deleteById(id);
    }
}