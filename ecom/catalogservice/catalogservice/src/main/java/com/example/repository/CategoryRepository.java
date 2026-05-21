package com.example.repository;


import com.example.entities.Category;
import io.micronaut.data.jdbc.annotation.JdbcRepository;
import io.micronaut.data.model.query.builder.sql.Dialect;
import io.micronaut.data.repository.CrudRepository;

import java.util.List;

@JdbcRepository(dialect = Dialect.MYSQL)
public interface CategoryRepository extends CrudRepository<Category,String> {
    @Override
    List<Category> findAll();

}
