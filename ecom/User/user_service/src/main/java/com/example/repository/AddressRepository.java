package com.example.repository;

import io.micronaut.data.annotation.Query;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.CrudRepository;
import com.example.entities.Address;

import java.util.List;

@Repository
public interface AddressRepository extends CrudRepository<Address, String> {

    List<Address> findAll();

    List<Address> findByUserId(String userId);
}