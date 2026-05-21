package controller;

import entity.Inventory;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.*;
import io.micronaut.scheduling.TaskExecutors;
import io.micronaut.scheduling.annotation.ExecuteOn;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecurityRule;
import service.InventoryService;

import java.util.Optional;

@Controller("/api/inventory")
@ExecuteOn(TaskExecutors.BLOCKING)
@Secured(SecurityRule.IS_ANONYMOUS)
public class InventoryController {

    private final InventoryService inventoryService;

    public InventoryController(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    @Get
    public Iterable<Inventory> getAllInventory() {
        return inventoryService.getAll();
    }

    @Get("/{id}")
    public HttpResponse<Inventory> getInventoryById(String id) {
        return inventoryService.getById(id)
                .map(HttpResponse::ok)
                .orElse(HttpResponse.notFound());
    }

    @Post
    public HttpResponse<Inventory> createInventory(@Body Inventory inventory) {
        Inventory saved = inventoryService.create(inventory);
        return HttpResponse.created(saved);
    }

    @Put("/{id}")
    public HttpResponse<Inventory> updateInventory(String id, @Body Inventory inventory) {
        Inventory updated = inventoryService.update(id, inventory);
        return HttpResponse.ok(updated);
    }

    @Delete("/{id}")
    public HttpResponse<?> deleteInventory(String id) {
        inventoryService.delete(id);
        return HttpResponse.noContent();
    }

    // NEW: stock update endpoint
    @Put("/{id}/stock")
    public HttpResponse<Inventory> updateStock(String id, @QueryValue int quantity) {
        Inventory updated = inventoryService.setStock(id, quantity);
        return HttpResponse.ok(updated);
    }
}