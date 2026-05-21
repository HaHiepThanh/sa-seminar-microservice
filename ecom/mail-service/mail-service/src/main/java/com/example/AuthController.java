package com.example;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Post;

import java.util.Random;

@Controller("/auth")
public class AuthController {
    private final EmailService emailService;

    // Inject EmailService vào Controller
    public AuthController(EmailService emailService) {
        this.emailService = emailService;
    }

    @Post("/login")
    public HttpResponse<String> login(@Body LoginRequest req) {

        String otp = String.valueOf(new Random().nextInt(899999) + 100000);

        emailService.sendOtpEmail(req.getEmail(), otp);

        // 4. Trả kết quả ngay lập tức cho Frontend
        return HttpResponse.ok("Đăng nhập thành công! Vui lòng kiểm tra email để nhận mã OTP.");
    }
}
