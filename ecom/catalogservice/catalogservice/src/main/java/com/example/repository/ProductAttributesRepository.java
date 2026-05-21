package com.example.repository;

import com.example.entities.ProductAttributes;
import io.micronaut.data.jdbc.annotation.JdbcRepository;
import io.micronaut.data.model.query.builder.sql.Dialect;
import io.micronaut.data.repository.CrudRepository;

@JdbcRepository(dialect = Dialect.MYSQL)
public interface ProductAttributesRepository extends CrudRepository<ProductAttributes, String> {
}
