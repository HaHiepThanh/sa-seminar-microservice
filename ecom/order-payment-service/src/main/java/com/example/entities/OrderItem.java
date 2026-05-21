package com.example.entities;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.UUID;
import io.micronaut.serde.annotation.Serdeable;


@Serdeable
@Entity
@Table(name = "order_items")
public class OrderItem {

    @Id
    @Column(columnDefinition = "CHAR(36)")
    private String id;

    @Column(name = "order_id")
    private String orderId;

    @Column(name = "product_id")
    private String productId;

    @Column(name = "product_name")
    private String productName;

    @Column(name = "unit_price")
    private BigDecimal unitPrice;

    private int quantity;

    // ===== Getter & Setter =====

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getOrderId() { return orderId; }
    public void setOrderId(String orderId) { this.orderId = orderId; }

    public String getProductId() { return productId; }
    public void setProductId(String productId) { this.productId = productId; }

    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }

    public BigDecimal getUnitPrice() { return unitPrice; }
    public void setUnitPrice(BigDecimal unitPrice) { this.unitPrice = unitPrice; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
}