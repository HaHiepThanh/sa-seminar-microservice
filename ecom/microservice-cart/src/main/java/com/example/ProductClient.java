package com.example;

import io.micronaut.http.annotation.Get;
import io.micronaut.http.client.annotation.Client;

@Client("http://catalog-service:8081")
public interface ProductClient {

    @Get("/api/products/{id}")
    ProductResponse getProductById(String id);
}
