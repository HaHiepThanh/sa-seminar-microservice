package com.example.service;

import com.example.dto.RegisterRequest;
import com.example.dto.UserRequest;
import com.example.entities.User;
import com.example.repository.UserRepository;
import jakarta.inject.Singleton;
import org.mindrot.jbcrypt.BCrypt;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Singleton
public class UserService {

    private final UserRepository userRepository;
    private final JwtService jwtService; // ✅ THÊM

    public UserService(UserRepository userRepository, JwtService jwtService) {
        this.userRepository = userRepository;
        this.jwtService = jwtService; // ✅ THÊM
    }

    // =========================
    // ✅ CREATE USER
    // =========================
    public User createUser(RegisterRequest request) {

        Optional<User> existing = userRepository.findByEmail(request.getEmail());
        if (existing.isPresent()) {
            throw new RuntimeException("Email already exists");
        }

        String hashedPassword = BCrypt.hashpw(request.getPassword(), BCrypt.gensalt());

        User user = new User();
        user.setId(UUID.randomUUID().toString());
        user.setEmail(request.getEmail());
        user.setPasswordHash(hashedPassword);

        return userRepository.save(user);
    }

    // =========================
    // ✅ GET ALL USERS
    // =========================
    public List<User> getAll() {
        return (List<User>) userRepository.findAll();
    }

    // =========================
    // ✅ LOGIN
    // =========================
    public Optional<User> login(String email, String password) {

        Optional<User> userOpt = userRepository.findByEmail(email);

        if (userOpt.isEmpty()) return Optional.empty();

        User user = userOpt.get();

        if (!BCrypt.checkpw(password, user.getPasswordHash())) {
            return Optional.empty();
        }

        return Optional.of(user);
    }
    public Optional<User> getUserFromToken(String token) {
        String email = jwtService.extractEmail(token);
        return userRepository.findByEmail(email);
    }
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }
}