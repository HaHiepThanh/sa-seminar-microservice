package entity;

import io.micronaut.serde.annotation.Serdeable;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Serdeable
@Entity
@Table(name = "inventory")
public class Inventory {

    @Id
    @Column(name = "product_id", length = 10)
    private String productId;

    @Column(nullable = false)
    private Integer quantity;

//    @Column(columnDefinition = "INT DEFAULT 0")
//    private Integer reserved;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // No-args constructor (required by JPA)
    public Inventory() {
    }

    // All-args constructor
//    public Inventory(String productId, Integer quantity, Integer reserved, LocalDateTime updatedAt) {
//        this.productId = productId;
//        this.quantity = quantity;
//        this.reserved = reserved;
//        this.updatedAt = updatedAt;
//    }

    // All-args constructor
    public Inventory(String productId, Integer quantity, LocalDateTime updatedAt) {
        this.productId = productId;
        this.quantity = quantity;
        this.updatedAt = updatedAt;
    }

    // Getters and Setters
    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

//    public Integer getReserved() {
//        return reserved;
//    }
//
//    public void setReserved(Integer reserved) {
//        this.reserved = reserved;
//    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}