package com.example;

import io.micronaut.serde.annotation.Serdeable;

import java.math.BigDecimal;

@Serdeable
public record OrderEmailEvent(
        String email,
        String customerName,
        String orderId,
        BigDecimal totalAmount
) {}
