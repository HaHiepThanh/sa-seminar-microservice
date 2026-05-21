package com.example.service;

import com.example.entities.User;
import com.nimbusds.jwt.JWTParser;
import io.micronaut.security.token.jwt.generator.JwtTokenGenerator;
import jakarta.inject.Singleton;
import io.micronaut.security.authentication.Authentication;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Singleton
public class JwtService {

    private final JwtTokenGenerator generator;

    public JwtService(JwtTokenGenerator generator) {
        this.generator = generator;
    }

    // ✅ GENERATE TOKEN
    public String generateToken(User user) {

        Map<String, Object> attributes = new HashMap<>();
        attributes.put("userId", user.getId());
        attributes.put("email", user.getEmail());

        Authentication authentication = Authentication.build(
                user.getEmail(),
                List.of(),
                attributes
        );

        return generator.generateToken(authentication, null)
                .orElseThrow(() -> new RuntimeException("Token error"));
    }
    // ✅ EXTRACT EMAIL
    public String extractEmail(String token) {
        try {
            return JWTParser.parse(token)
                    .getJWTClaimsSet()
                    .getSubject();
        } catch (Exception e) {
            throw new RuntimeException("Invalid JWT");
        }
    }
}