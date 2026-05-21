package com.example.dto;

import java.math.BigDecimal;

public class OrderCreatedEvent {

    private String orderId;
    private String userId;
    private BigDecimal totalPrice;

    public OrderCreatedEvent() {}

    public OrderCreatedEvent(String orderId, String userId, BigDecimal totalPrice) {
        this.orderId = orderId;
        this.userId = userId;
        this.totalPrice = totalPrice;
    }

    public String getOrderId() { return orderId; }
    public void setOrderId(String orderId) { this.orderId = orderId; }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public BigDecimal getTotalPrice() { return totalPrice; }
    public void setTotalPrice(BigDecimal totalPrice) { this.totalPrice = totalPrice; }
}