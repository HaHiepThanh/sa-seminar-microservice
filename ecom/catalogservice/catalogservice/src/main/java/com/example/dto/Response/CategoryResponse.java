package com.example.dto.Response;


import io.micronaut.serde.annotation.Serdeable;

@Serdeable
public class CategoryResponse {
    String id;
    String name;

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
}
