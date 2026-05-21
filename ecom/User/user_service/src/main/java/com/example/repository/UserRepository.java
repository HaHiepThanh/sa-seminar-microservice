package com.example.repository;

import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.CrudRepository;
import com.example.entities.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<User, String> {

    List<User> findAll(); // no need

    Optional<User> findByEmail(String email);
    // base on method's name => select * from User where email = ?
}