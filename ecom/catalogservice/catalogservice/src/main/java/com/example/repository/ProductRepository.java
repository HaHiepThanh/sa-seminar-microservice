package com.example.repository;

import com.example.entities.Product;
import io.micronaut.data.annotation.Join;
import io.micronaut.data.jdbc.annotation.JdbcRepository;
import io.micronaut.data.model.Page;
import io.micronaut.data.model.Pageable;
import io.micronaut.data.model.query.builder.sql.Dialect;
import io.micronaut.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

@JdbcRepository(dialect =  Dialect.MYSQL)
public interface ProductRepository extends CrudRepository<Product, String> {
    @Join("category")
    Page<Product> findAll(Pageable pageable);

    @Override
    @Join("category")
    List<Product> findAll();

    List<Product> findByCategoryId(String categoryId);

    @Join("category")
    @Override
    Optional<Product> findById(String id);
}
