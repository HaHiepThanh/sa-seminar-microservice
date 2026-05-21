package com.example.service;

import com.example.dto.UserProfileRequest;
import com.example.entities.User;
import com.example.entities.UserProfile;
import com.example.repository.UserProfileRepository;
import jakarta.inject.Singleton;
import org.mindrot.jbcrypt.BCrypt;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
@Singleton
public class UserProfileService {

    private final UserProfileRepository profileRepository;

    public UserProfileService(UserProfileRepository profileRepository) {
        this.profileRepository = profileRepository;
    }

    public UserProfile createProfile(String userId, UserProfileRequest request) {

        UserProfile profile = new UserProfile();
        profile.setUserId(userId);
        profile.setFullName(request.getFullName());
        profile.setPhone(request.getPhone());

        return profileRepository.save(profile);
    }
    public void updateProfile(String userId, UserProfileRequest req) {

        Optional<UserProfile> existing = profileRepository.findByUserId(userId);

        UserProfile profile = existing.orElse(new UserProfile());

        profile.setUserId(userId);
        profile.setFullName(req.getFullName());
        profile.setPhone(req.getPhone());

        profileRepository.update(profile); // hoặc save tùy ORM
    }
    public void updateAvatar(String userId, String avatarUrl) {

        UserProfile profile = profileRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Profile not found"));

        profile.setAvatarUrl(avatarUrl);

        profileRepository.update(profile); // 🔥 KHÔNG SAVE
    }

    public Optional<UserProfile> getByUserId(String userId) {
        return profileRepository.findById(userId);
    }
    public List<UserProfile> getAllProfiles() {
        return (List<UserProfile>) profileRepository.findAll();
    }

}