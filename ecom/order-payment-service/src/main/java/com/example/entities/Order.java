package com.example.entities;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import io.micronaut.serde.annotation.Serdeable;

@Serdeable
@Entity
@Table(name = "orders")
public class Order {

    @Id
    @Column(name = "id", columnDefinition = "CHAR(36)", insertable = false, updatable = false)
    private String id;

    @Column(name = "user_id")
    private String userId;

    private String phone;
    private String status;

    @Column(name = "total_price")
    private BigDecimal totalPrice;

    @Column(name = "address_line")
    private String addressLine;

    private String city;
    private String country;

    @Column(name = "created_at", insertable = false, updatable = false)
    private LocalDateTime createdAt;

    // ===== Getter & Setter (Giữ nguyên các hàm bạn đã viết) =====
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public BigDecimal getTotalPrice() { return totalPrice; }
    public void setTotalPrice(BigDecimal totalPrice) { this.totalPrice = totalPrice; }
    public String getAddressLine() { return addressLine; }
    public void setAddressLine(String addressLine) { this.addressLine = addressLine; }
    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }
    public String getCountry() { return country; }
    public void setCountry(String country) { this.country = country; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}