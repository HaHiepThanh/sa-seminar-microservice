package repository;

import entity.InventoryLog;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.CrudRepository;

@Repository
public interface InventoryLogRepository extends CrudRepository<InventoryLog, String> {
}