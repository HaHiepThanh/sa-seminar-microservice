package com.example.entities;
import io.micronaut.data.annotation.MappedEntity;
import jakarta.persistence.*;
import io.micronaut.serde.annotation.Serdeable;

@Serdeable
@Entity
@Table(name = "user_profiles")
@MappedEntity
public class UserProfile {

    @Id
    @Column(name = "user_id", columnDefinition = "CHAR(36)")
    private String userId;

    @Column(name = "full_name")
    private String fullName;

    private String phone;

    @Column(name = "avatar_url")
    private String avatarUrl;

    // ===== Getter & Setter =====
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getAvatarUrl() { return avatarUrl; }
    public void setAvatarUrl(String avatarUrl) { this.avatarUrl = avatarUrl; }
}