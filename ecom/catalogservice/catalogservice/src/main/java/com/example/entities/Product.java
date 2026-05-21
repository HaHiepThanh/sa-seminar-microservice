package com.example.entities;

import io.micronaut.data.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@MappedEntity("products")
public class Product {
    @Id
//    @GeneratedValue(GeneratedValue.Type.UUID)
    String id;
    String name;

    @Relation(value = Relation.Kind.MANY_TO_ONE)
            @MappedProperty("category_id")
   Category category;
    private String description;
    private String imageUrl;
    private BigDecimal price;
    @DateCreated
    private LocalDateTime createdAt;

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }




    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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
