package com.example.dto.Response;

import io.micronaut.core.annotation.Introspected;

import java.math.BigDecimal;

@Introspected
public class ProductResponseCart {
    String name;
    String imageUrl;
    BigDecimal price;

    public ProductResponseCart(String name, String imageUrl, BigDecimal price) {
        this.name = name;
        this.imageUrl = imageUrl;
        this.price = price;
    }

    public ProductResponseCart() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }
}
