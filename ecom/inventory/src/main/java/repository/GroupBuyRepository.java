package repository;

import entity.GroupBuy;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.CrudRepository;
import java.util.Optional;

@Repository
public interface GroupBuyRepository extends CrudRepository<GroupBuy, String> {
    Optional<GroupBuy> findByProductIdAndStatus(String productId, String status);
}
