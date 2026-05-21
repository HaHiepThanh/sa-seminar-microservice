package com.example.dto;
import io.micronaut.core.annotation.Introspected;
import io.micronaut.serde.annotation.Serdeable;
@Introspected
@Serdeable
public class UserProfileRequest {
    private String fullName;
    private String phone;
    private String avatarUrl;

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }
    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
}