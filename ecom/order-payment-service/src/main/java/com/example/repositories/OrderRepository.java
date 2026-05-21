package com.example.repositories;

import io.micronaut.data.annotation.Repository; // Dùng cái này thay Jdbc
import io.micronaut.data.repository.CrudRepository;
import com.example.entities.Order;
import java.util.List;
import java.util.Collections;


@Repository
public interface OrderRepository extends CrudRepository<Order, String> {
    List<Order> findAll();
}