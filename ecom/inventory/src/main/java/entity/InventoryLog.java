package entity;

import io.micronaut.serde.annotation.Serdeable;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Serdeable
@Entity
@Table(name = "inventory_logs")
public class InventoryLog {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(columnDefinition = "CHAR(36)", updatable = false, nullable = false)
    private String id;

    @Column(name = "product_id", length = 10)
    private String productId;

    @Column(name = "change_value")
    private Integer changeValue;

    @Column(length = 20)
    private String reason; // e.g., 'ORDER', 'CANCEL', 'RESTOCK'

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    // Automatically set the created_at timestamp before saving if not set by the DB
    @PrePersist
    protected void onCreate() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
    }

    // Constructors
    public InventoryLog() {}

    public InventoryLog(String productId, Integer changeValue, String reason) {
        this.productId = productId;
        this.changeValue = changeValue;
        this.reason = reason;
    }

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getProductId() { return productId; }
    public void setProductId(String productId) { this.productId = productId; }

    public Integer getChangeValue() { return changeValue; }
    public void setChangeValue(Integer changeValue) { this.changeValue = changeValue; }

    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
