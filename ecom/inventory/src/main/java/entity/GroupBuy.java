package entity;

import io.micronaut.serde.annotation.Serdeable;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Serdeable
@Entity
@Table(name = "group_buys")
public class GroupBuy {

    public static final String STATUS_OPEN = "OPEN";
    public static final String STATUS_SUCCESS = "SUCCESS";
    public static final String STATUS_FAILED = "FAILED";

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(columnDefinition = "CHAR(36)", updatable = false, nullable = false)
    private String id;

    @Column(name = "product_id", length = 10, nullable = false)
    private String productId;

    @Column(name = "target_quantity", nullable = false)
    private Integer targetQuantity;

    @Column(name = "current_quantity", nullable = false)
    private Integer currentQuantity = 0;

    @Column(nullable = false)
    private String status = STATUS_OPEN;

    @Column(name = "start_date")
    private LocalDateTime startDate;

    @Column(name = "end_date")
    private LocalDateTime endDate;

    @Column(name = "created_at", insertable = false, updatable = false)
    private LocalDateTime createdAt;

    // No-args constructor
    public GroupBuy() {}

    // Constructor (optional convenience)
    public GroupBuy(String productId, Integer targetQuantity,
                    LocalDateTime startDate, LocalDateTime endDate) {
        this.productId = productId;
        this.targetQuantity = targetQuantity;
        this.currentQuantity = 0;
        this.status = STATUS_OPEN;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    // Getters & Setters

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public Integer getTargetQuantity() {
        return targetQuantity;
    }

    public void setTargetQuantity(Integer targetQuantity) {
        this.targetQuantity = targetQuantity;
    }

    public Integer getCurrentQuantity() {
        return currentQuantity;
    }

    public void setCurrentQuantity(Integer currentQuantity) {
        this.currentQuantity = currentQuantity;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}