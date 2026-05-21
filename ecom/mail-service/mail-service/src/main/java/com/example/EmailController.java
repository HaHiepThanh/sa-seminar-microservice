package com.example;

import io.micronaut.http.annotation.*;
import jakarta.inject.Inject;

@Controller("/api/email")
public class EmailController {

    @Inject
    private EmailService emailService;

    // Endpoint to send order confirmation email
    @Post("/order-confirmation")
    public String sendOrderConfirmation(@Body OrderEmailRequest request) {
        emailService.sendOrderConfirmationEmail(
                request.getEmail(),
                request.getCustomerName(),
                request.getOrderId(),
                request.getTotalAmount()
        );

        return "Order confirmation email is being sent!";
    }
}
