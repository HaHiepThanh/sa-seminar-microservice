package dto.request;

import java.time.LocalDateTime;

public class CreateGroupBuyRequest {

    private String productId;
    private Integer targetQuantity;
    private LocalDateTime startDate;
    private LocalDateTime endDate;

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
}
