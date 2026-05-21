package com.example.entities;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import io.micronaut.serde.annotation.Serdeable;

@Serdeable
@Entity
@Table(name = "users")
public class User {

    @Id
    @Column(name = "id", columnDefinition = "CHAR(36)", insertable = false, updatable = false)
    private String id;

    @Column(name = "email")
    private String email;

    @Column(name = "password_hash")
    private String passwordHash;

    @Column(name = "created_at", insertable = false, updatable = false)
    private LocalDateTime createdAt;

    // ===== Getter & Setter =====
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPasswordHash() { return passwordHash; }
    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}