package com.example.controllers;
import com.example.entities.Payment;
import com.example.services.PaymentService;
import com.example.dto.CreatePaymentRequest;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.*;

import java.util.List;
import java.util.Optional;


@Controller("/api/payments")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    // ===== GET ALL =====
    @Get
    public HttpResponse<List<Payment>> getAll() {
        return HttpResponse.ok(paymentService.getAll());
    }

    // ===== GET BY ID =====
    @Get("/{id}")
    public HttpResponse<?> getById(@PathVariable String id) {
        return paymentService.getById(id)
                .<HttpResponse<?>>map(HttpResponse::ok)
                .orElseGet(HttpResponse::notFound);
    }

    // ===== CREATE =====
    @Post
    public HttpResponse<Payment> create(@Body CreatePaymentRequest request) {
        return HttpResponse.created(paymentService.create(request));
    }

    // ===== UPDATE =====
    @Put("/{id}")
    public HttpResponse<?> update(@PathVariable String id,
                                  @Body CreatePaymentRequest request) {
        try {
            return HttpResponse.ok(paymentService.update(id, request));
        } catch (Exception e) {
            return HttpResponse.notFound();
        }
    }

    // ===== DELETE =====
    @Delete("/{id}")
    public HttpResponse<?> delete(@PathVariable String id) {
        paymentService.delete(id);
        return HttpResponse.ok("Deleted");
    }

    // ===== EXTRA =====
    @Get("/order/{orderId}")
    public HttpResponse<List<Payment>> getByOrder(@PathVariable String orderId) {
        return HttpResponse.ok(paymentService.getByOrderId(orderId));
    }
}
