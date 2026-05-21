package com.example;

import io.micronaut.context.annotation.Value;
import io.micronaut.scheduling.annotation.Async;
import jakarta.inject.Singleton;
import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

import java.math.BigDecimal;
import java.util.Properties;

@Singleton
public class EmailService {

    @Value("${mail.smtp.host}")
    private String smtpHost;

    @Value("${mail.smtp.port}")
    private String smtpPort;

    @Value("${mail.smtp.username}")
    private String username;

    @Value("${mail.smtp.password}")
    private String password;

    @Async
    public void sendOtpEmail(String toEmail, String otp) {
        System.out.println("⏳ Đang gửi email ngầm đến: " + toEmail);

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.host", smtpHost);
        props.put("mail.smtp.port", smtpPort);
        if ("465".equals(smtpPort)) {
            props.put("mail.smtp.ssl.enable", "true");
            props.put("mail.smtp.ssl.trust", smtpHost);
        } else {
            props.put("mail.smtp.starttls.enable", "true");
        }

        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        try {
            Message mimeMessage = new MimeMessage(session);
            mimeMessage.setFrom(new InternetAddress(username, "MiniStore Security"));
            mimeMessage.setRecipient(Message.RecipientType.TO, new InternetAddress(toEmail));
            mimeMessage.setSubject("Mã xác nhận đăng nhập MiniStore");

            String htmlContent = "<h2>Chào bạn,</h2>"
                    + "<p>Mã OTP đăng nhập của bạn là: <b style='font-size:20px; color:blue;'>" + otp + "</b></p>"
                    + "<p>Tuyệt đối không chia sẻ mã này cho người khác.</p>";
            mimeMessage.setContent(htmlContent, "text/html; charset=UTF-8");

            Transport.send(mimeMessage);
            System.out.println("✅ Đã gửi email thành công!");

        } catch (Exception e) {
            System.err.println("❌ Lỗi khi gửi email: " + e.getMessage());
        }
    }

    @Async
    public void sendOrderConfirmationEmail(String toEmail, String customerName, String orderId, BigDecimal totalAmount) {
        System.out.println("⏳ Sending order confirmation to: " + toEmail);

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.host", smtpHost);
        props.put("mail.smtp.port", smtpPort);
        if ("465".equals(smtpPort)) {
            props.put("mail.smtp.ssl.enable", "true");
            props.put("mail.smtp.ssl.trust", smtpHost);
        } else {
            props.put("mail.smtp.starttls.enable", "true");
        }

        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        try {
            Message mimeMessage = new MimeMessage(session);
            mimeMessage.setFrom(new InternetAddress(username, "MiniStore"));
            mimeMessage.setRecipient(Message.RecipientType.TO, new InternetAddress(toEmail));
            mimeMessage.setSubject("🛒 Order Confirmation - MiniStore");

            String htmlContent = "<h2>Hi " + customerName + ",</h2>"
                    + "<p>🎉 Your order has been placed successfully!</p>"
                    + "<p><b>Order ID:</b> " + orderId + "</p>"
                    + "<p><b>Total Amount:</b> $" + totalAmount + "</p>"
                    + "<hr>"
                    + "<p>We will process your order soon. Thank you for shopping with us!</p>"
                    + "<p>— MiniStore Team</p>";

            mimeMessage.setContent(htmlContent, "text/html; charset=UTF-8");

            Transport.send(mimeMessage);
            System.out.println("✅ Order confirmation email sent!");

        } catch (Exception e) {
            System.err.println("❌ Failed to send order email: " + e.getMessage());
            e.printStackTrace();
        }
    }
}