package com.example;


import jakarta.inject.Singleton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Singleton
public class CartAggregationService {

    private final DatabaseHealthService databaseHealthService;
    private final ProductClient productClient;

    public CartAggregationService(DatabaseHealthService databaseHealthService,
                                  ProductClient productClient) {
        this.databaseHealthService = databaseHealthService;
        this.productClient = productClient;
    }

    public List<Map<String, Object>> getCartWithProduct(String userId) throws Exception {

        List<Map<String, Object>> cartItems =
                databaseHealthService.cartItemsByUser(userId, 100);

        List<Map<String, Object>> result = new ArrayList<>();

        for (Map<String, Object> item : cartItems) {

            String productId = (String) item.get("productId");

            ProductResponse product = productClient.getProductById(productId);

            Map<String, Object> merged = new HashMap<>();

            merged.put("id", item.get("id"));
            merged.put("productId", product.getId());
            merged.put("name", product.getName());
            merged.put("price", product.getPrice());
            merged.put("imageUrl", product.getImageUrl());
            merged.put("quantity", item.get("quantity"));

            result.add(merged);
        }

        return result;
    }
}
