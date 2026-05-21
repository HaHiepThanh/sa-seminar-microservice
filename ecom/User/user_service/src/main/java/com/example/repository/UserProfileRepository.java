package com.example.repository;

import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.CrudRepository;
import com.example.entities.UserProfile;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserProfileRepository extends CrudRepository<UserProfile, String> {
    Optional<UserProfile> findByUserId(String userId);
}