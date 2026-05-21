package com.example;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.*;
import io.micronaut.scheduling.TaskExecutors;
import io.micronaut.scheduling.annotation.ExecuteOn;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.authentication.Authentication;
import io.micronaut.security.rules.SecurityRule;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Secured(SecurityRule.IS_AUTHENTICATED)
//@Secured(SecurityRule.IS_ANONYMOUS)
@Controller("/api/cart")
@ExecuteOn(TaskExecutors.BLOCKING)
public class CartController {

    private final DatabaseHealthService databaseHealthService;
    private final CartAggregationService cartAggregationService;

    public CartController(DatabaseHealthService databaseHealthService, CartAggregationService cartAggregationService) {
        this.databaseHealthService = databaseHealthService;
        this.cartAggregationService = cartAggregationService;
    }

    /** GET /api/carts — Lấy toàn bộ giỏ hàng */
    @ExecuteOn(TaskExecutors.BLOCKING)
    @Get
    @Produces(MediaType.APPLICATION_JSON)
    public List<Map<String, Object>> items(@QueryValue(defaultValue = "100") int limit) throws SQLException {
        return databaseHealthService.cartItems(limit);
    }

    /** GET /api/carts/{userId} — Lấy giỏ hàng theo userId */
    @ExecuteOn(TaskExecutors.BLOCKING)
    @Get("/user")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Map<String, Object>> itemsByUser(Authentication authentication,
            @QueryValue(defaultValue = "100") int limit) throws SQLException {
        return databaseHealthService.cartItemsByUser((String) authentication.getAttributes().get("userId"), limit);
    }

    /** POST /api/carts — Thêm sản phẩm vào giỏ */
    @ExecuteOn(TaskExecutors.BLOCKING)
    @Post
    @Produces(MediaType.APPLICATION_JSON)
    public HttpResponse<?> createItem(@Body CartItemCreateRequest request, Authentication authentication) throws SQLException {
        if (request.getProductId() == null || request.getProductId().isBlank()
                || request.getQuantity() <= 0) {
            return HttpResponse.badRequest(Map.of("message", "userId, productId and quantity > 0 are required"));
        }

        String userId = (String) authentication.getAttributes().get("userId");
        Map<String, Object> item = databaseHealthService.createCartItem(
                UUID.randomUUID().toString(),
                userId,
                request.getProductId(),
                request.getQuantity());

        return HttpResponse.status(HttpStatus.CREATED).body(item);
    }

    /** PUT /api/carts/{id} — Cập nhật số lượng */
    @ExecuteOn(TaskExecutors.BLOCKING)
    @Put("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public HttpResponse<?> updateItemQuantity(@PathVariable String id,
            @Body CartItemUpdateRequest request) throws SQLException {
        if (request.getQuantity() <= 0) {
            return HttpResponse.badRequest(Map.of("message", "quantity must be > 0"));
        }

        Map<String, Object> existing = databaseHealthService.getCartItemById(id);
        if (existing == null) {
            return HttpResponse.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", "cart item not found", "id", id));
        }

        Map<String, Object> updated = databaseHealthService.updateCartItemQuantity(id, request.getQuantity());
        return HttpResponse.ok(updated);
    }

    /** DELETE /api/carts/{id} — Xoá sản phẩm khỏi giỏ */
    @Delete("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public HttpResponse<?> deleteItem(@PathVariable String id) throws SQLException {
        boolean deleted = databaseHealthService.deleteCartItem(id);
        if (!deleted) {
            return HttpResponse.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", "cart item not found", "id", id));
        }

        Map<String, Object> response = new HashMap<>();
        response.put("message", "cart item deleted");
        response.put("id", id);
        return HttpResponse.ok(response);
    }

    @Get("/details")
    public List<Map<String, Object>> getCartDetails(Authentication authentication) throws Exception {

        String userId = (String) authentication.getAttributes().get("userId");

        return cartAggregationService.getCartWithProduct(userId);
    }

    @Delete("/clear")
    @Produces(MediaType.APPLICATION_JSON)
    public HttpResponse<?> clearCart(Authentication authentication) throws SQLException {

        String userId = (String) authentication.getAttributes().get("userId");

        databaseHealthService.clearCartByUser(userId);

        return HttpResponse.ok(Map.of(
                "message", "cart cleared",
                "userId", userId
        ));
    }
}