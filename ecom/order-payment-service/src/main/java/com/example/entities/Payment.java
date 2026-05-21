package com.example.entities;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;
import io.micronaut.serde.annotation.Serdeable;


@Serdeable

@Entity
@Table(name = "payments")
public class Payment {

    @Id
    @Column(columnDefinition = "CHAR(36)")
    private String id;

    @Column(name = "order_id")
    private String orderId;

    @Column(name = "payment_method")
    private String paymentMethod;

    private String status;

    @Column(name = "paid_at")
    private LocalDateTime paidAt;

    // ===== Getter & Setter =====

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getOrderId() { return orderId; }
    public void setOrderId(String orderId) { this.orderId = orderId; }

    public String getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public LocalDateTime getPaidAt() { return paidAt; }
    public void setPaidAt(LocalDateTime paidAt) { this.paidAt = paidAt; }
}