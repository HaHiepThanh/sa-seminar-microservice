package com.example.repositories;
import io.micronaut.data.annotation.Repository; // Dùng cái này thay Jdbc
import io.micronaut.data.repository.CrudRepository;
import com.example.entities.OrderItem;


@Repository
public interface OrderItemRepository extends CrudRepository<OrderItem, String> {
    void deleteByOrderId(String orderId);
}