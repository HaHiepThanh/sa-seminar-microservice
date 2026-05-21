package com.example.services;

import com.example.entities.Payment;
import com.example.repositories.PaymentRepository;
import com.example.dto.CreatePaymentRequest;

import jakarta.inject.Singleton;
import jakarta.transaction.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Singleton
public class PaymentService {

    private final PaymentRepository paymentRepository;

    public PaymentService(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    // ===== CREATE =====
    @Transactional
    public Payment create(CreatePaymentRequest request) {

        Payment payment = new Payment();

        payment.setId(UUID.randomUUID().toString());
        payment.setOrderId(request.getOrderId());
        payment.setPaymentMethod(request.getPaymentMethod());
        payment.setStatus(request.getStatus());

        // auto set time
        payment.setPaidAt(LocalDateTime.now());

        // validate
        if (!"SUCCESS".equals(request.getStatus()) &&
                !"FAILED".equals(request.getStatus())) {
            throw new RuntimeException("Invalid status");
        }

        return paymentRepository.save(payment);
    }

    // ===== READ =====
    public List<Payment> getAll() {
        return (List<Payment>) paymentRepository.findAll();
    }

    public Optional<Payment> getById(String id) {
        return paymentRepository.findById(id);
    }

    // ===== UPDATE =====
    @Transactional
    public Payment update(String id, CreatePaymentRequest request) {

        Payment existing = paymentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Payment not found"));

        existing.setPaymentMethod(request.getPaymentMethod());
        existing.setStatus(request.getStatus());

        return paymentRepository.update(existing);
    }

    // ===== DELETE =====
    @Transactional
    public void delete(String id) {
        paymentRepository.deleteById(id);
    }

    public List<Payment> getByOrderId(String orderId) {
        return paymentRepository.findByOrderId(orderId);
    }
}