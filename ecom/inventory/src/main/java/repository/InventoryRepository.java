package repository;


import entity.Inventory;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.CrudRepository;
import java.util.Optional;

@Repository
public interface InventoryRepository extends CrudRepository<Inventory, String> {
    Inventory findByProductId(String productId);
}
