package com.example.repositories;
import io.micronaut.data.annotation.Repository; // Dùng cái này thay Jdbc
import io.micronaut.data.repository.CrudRepository;
import com.example.entities.Payment;

import java.util.List;

@Repository
public interface PaymentRepository extends CrudRepository<Payment, String> {
    List<Payment> findByOrderId(String orderId);
    void deleteByOrderId(String orderId);
}