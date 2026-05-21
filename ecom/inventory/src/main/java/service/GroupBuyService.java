package service;

import entity.GroupBuy;
import jakarta.inject.Singleton;
import jakarta.transaction.Transactional;
import repository.GroupBuyRepository;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Singleton
public class GroupBuyService {

    private final GroupBuyRepository groupBuyRepository;
    private final InventoryService inventoryService;
    private final InventoryGrpcService grpcService;

    public GroupBuyService(GroupBuyRepository groupBuyRepository,
                           InventoryService inventoryService,
                           InventoryGrpcService grpcService) {
        this.groupBuyRepository = groupBuyRepository;
        this.inventoryService = inventoryService;
        this.grpcService = grpcService;
    }

    // =========================
    // CREATE GROUP BUY
    // =========================
    public GroupBuy createGroupBuy(String productId, int targetQty, LocalDateTime endDate) {

        GroupBuy gb = new GroupBuy();
        gb.setId(UUID.randomUUID().toString());
        gb.setProductId(productId);
        gb.setTargetQuantity(targetQty);
        gb.setCurrentQuantity(0);
        gb.setStatus(GroupBuy.STATUS_OPEN);
        gb.setStartDate(LocalDateTime.now());
        gb.setEndDate(endDate);

        GroupBuy saved = groupBuyRepository.save(gb);

        // 🔥 optional: stream initial state
        GroupBuy fresh = groupBuyRepository.findById(saved.getId())
                .orElseThrow();

        grpcService.pushGroupBuyUpdate(fresh);

        return saved;
    }

    public Optional<GroupBuy> getByProductIdAndStatus(String productId, String status) {
        return groupBuyRepository.findByProductIdAndStatus(productId, status);
    }

    public void increaseIfOpen(String productId, int quantity) {

        groupBuyRepository.findByProductIdAndStatus(productId, "OPEN")
                .ifPresent(gb -> {

                    gb.setCurrentQuantity(gb.getCurrentQuantity() + quantity);

                    if (gb.getCurrentQuantity() >= gb.getTargetQuantity()) {
                        gb.setStatus("SUCCESS");
                    }

                    groupBuyRepository.update(gb);

                    System.out.println("🎯 GroupBuy updated: " + gb.getId());
                });
    }

    // =========================
    // JOIN GROUP BUY
    // =========================
    @Transactional
    public void joinGroupBuy(String groupBuyId, int quantity) {

        GroupBuy gb = getById(groupBuyId);

        if (!GroupBuy.STATUS_OPEN.equals(gb.getStatus())) {
            throw new RuntimeException("Group buy not open");
        }

        // 1. reserve stock
        inventoryService.reserveStock(gb.getProductId(), quantity);

        // 2. update group buy
        gb.setCurrentQuantity(gb.getCurrentQuantity() + quantity);

        if (gb.getCurrentQuantity() >= gb.getTargetQuantity()) {
            gb.setStatus(GroupBuy.STATUS_SUCCESS);
        }

        groupBuyRepository.update(gb);

        // re-fetch fresh DB state before streaming
        GroupBuy fresh = groupBuyRepository.findById(gb.getId())
                .orElseThrow();

        grpcService.pushGroupBuyUpdate(fresh);
    }

    // =========================
    // GET BY ID
    // =========================
    public GroupBuy getById(String id) {
        return groupBuyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Group buy not found"));
    }

    // =========================
    // CLOSE MANUALLY
    // =========================
    @Transactional
    public void closeGroupBuy(String id) {

        GroupBuy gb = getById(id);

        if (!GroupBuy.STATUS_OPEN.equals(gb.getStatus())) {
            throw new RuntimeException("Group buy not open");
        }

        gb.setStatus(GroupBuy.STATUS_FAILED);

        groupBuyRepository.update(gb);

        GroupBuy fresh = groupBuyRepository.findById(gb.getId())
                .orElseThrow();

        grpcService.pushGroupBuyUpdate(fresh);
    }

    // =========================
    // AUTO EXPIRE
    // =========================
    @Transactional
    public void closeExpired(GroupBuy gb) {

        if (GroupBuy.STATUS_OPEN.equals(gb.getStatus())
                && gb.getEndDate().isBefore(LocalDateTime.now())) {

            gb.setStatus(GroupBuy.STATUS_FAILED);

            groupBuyRepository.update(gb);

            GroupBuy fresh = groupBuyRepository.findById(gb.getId())
                    .orElseThrow();

            grpcService.pushGroupBuyUpdate(fresh);
        }
    }
}