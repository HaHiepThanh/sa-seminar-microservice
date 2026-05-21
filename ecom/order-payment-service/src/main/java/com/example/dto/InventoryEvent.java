package com.example.dto;

import io.micronaut.serde.annotation.Serdeable;
import java.util.List;

@Serdeable
public record InventoryEvent(
        String orderId,
        List<Item> items
) {
    @Serdeable
    public record Item(
            String productId,
            int quantity
    ) {}
}