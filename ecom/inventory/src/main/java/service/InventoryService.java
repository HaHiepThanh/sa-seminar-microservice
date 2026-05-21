package service;

import entity.Inventory;
import entity.GroupBuy;
import jakarta.inject.Singleton;
import jakarta.transaction.Transactional;
import repository.InventoryRepository;
import repository.GroupBuyRepository;

import java.util.Optional;

@Singleton
public class InventoryService {

    private final InventoryRepository inventoryRepository;
    private final GroupBuyRepository groupBuyRepository;
    private final InventoryGrpcService grpcService;

    public InventoryService(
            InventoryRepository inventoryRepository,
            GroupBuyRepository groupBuyRepository,
            InventoryGrpcService grpcService) {

        this.inventoryRepository = inventoryRepository;
        this.groupBuyRepository = groupBuyRepository;
        this.grpcService = grpcService;
    }

    // ---------------- READ ----------------
    public Iterable<Inventory> getAll() {
        return inventoryRepository.findAll();
    }

    public Optional<Inventory> getById(String id) {
        return inventoryRepository.findById(id);
    }

    // ---------------- CREATE ----------------
    @Transactional
    public Inventory create(Inventory inventory) {
        return inventoryRepository.save(inventory);
    }

    // ---------------- UPDATE ----------------
    @Transactional
    public Inventory update(String id, Inventory inventory) {
        inventory.setProductId(id);
        Inventory updated = inventoryRepository.update(inventory);
        //notifyStream(id);
        return updated;
    }

    public void decreaseStock(String productId, int quantity) {
        Inventory inventory = inventoryRepository.findByProductId(productId);


        inventory.setQuantity(inventory.getQuantity() - quantity);
        inventoryRepository.update(inventory);

        System.out.println("✅ Stock updated for product: " + productId);
    }

    // ---------------- DELETE ----------------
    @Transactional
    public void delete(String id) {
        inventoryRepository.deleteById(id);
        //notifyStream(id);
    }

    // ---------------- STOCK SET ----------------
    @Transactional
    public Inventory setStock(String id, int quantity) {

        Inventory inv = inventoryRepository.findById(id)
                .orElseThrow();

        inv.setQuantity(quantity);

        Inventory updated = inventoryRepository.update(inv);

        //notifyStream(id);

        return updated;
    }

    // ---------------- RESERVE STOCK (MISSING BEFORE) ----------------
    @Transactional
    public boolean reserveStock(String productId, int amount) {

        Inventory inv = inventoryRepository.findById(productId)
                .orElseThrow();

        if (inv.getQuantity() < amount) {
            return false;
        }

        inv.setQuantity(inv.getQuantity() - amount);
        inventoryRepository.update(inv);

        //notifyStream(productId);

        return true;
    }



    // ---------------- STREAM NOTIFY ----------------
//    private void notifyStream(String productId) {
//
//        groupBuyRepository.findByProductIdAndStatus(productId, "OPEN")
//                .ifPresent(grp -> grpcService.pushGroupBuyUpdate(grp));
//    }
}