package com.example.entities;

import io.micronaut.data.annotation.Id;
import io.micronaut.data.annotation.MappedEntity;
import io.micronaut.data.annotation.MappedProperty;
import io.micronaut.data.annotation.Relation;

@MappedEntity("product_attributes")
public class ProductAttributes {
    @Id
    String id;

    @Relation(Relation.Kind.MANY_TO_ONE)
    @MappedProperty("product_id")
    Product product;

    @MappedProperty("attribute_name")
    String attribute_name;

    @MappedProperty("attribute_value")
    String attribute_value;

    int attribute_type;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public String getAttribute_name() {
        return attribute_name;
    }

    public void setAttribute_name(String attribute_name) {
        this.attribute_name = attribute_name;
    }

    public String getAttribute_value() {
        return attribute_value;
    }

    public void setAttribute_value(String attribute_value) {
        this.attribute_value = attribute_value;
    }

    public int getAttribute_type() {
        return attribute_type;
    }

    public void setAttribute_type(int attribute_type) {
        this.attribute_type = attribute_type;
    }
}
