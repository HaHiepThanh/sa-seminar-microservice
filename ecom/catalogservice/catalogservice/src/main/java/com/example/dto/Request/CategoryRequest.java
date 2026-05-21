package com.example.dto.Request;

import io.micronaut.serde.annotation.Serdeable;

@Serdeable
public class CategoryRequest {
    String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
