package com.example.dto;

import java.util.List;
import io.micronaut.core.annotation.Introspected;
import io.micronaut.serde.annotation.Serdeable;


@Serdeable
@Introspected
public class CreatePaymentRequest {

    private String orderId;
    private String paymentMethod;
    private String status;

    public String getOrderId() { return orderId; }
    public void setOrderId(String orderId) { this.orderId = orderId; }

    public String getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}