package com.example.dto.Request;

import io.micronaut.serde.annotation.Serdeable;

@Serdeable
public class ProductAttributesRequest {
    String idProduct;
    @com.fasterxml.jackson.annotation.JsonProperty("attribute_name")
    String  attributeName;
    @com.fasterxml.jackson.annotation.JsonProperty("attribute_value")
    String attributeValue;

    @com.fasterxml.jackson.annotation.JsonProperty("attribute_type")
    int attributeType;

    public String getIdProduct() {
        return idProduct;
    }

    public void setIdProduct(String idProduct) {
        this.idProduct = idProduct;
    }

    public String getAttributeName() {
        return attributeName;
    }

    public void setAttributeName(String attributeName) {
        this.attributeName = attributeName;
    }

    public int getAttributeType() {
        return attributeType;
    }

    public void setAttributeType(int attributeType) {
        this.attributeType = attributeType;
    }

    public String getAttributeValue() {
        return attributeValue;
    }

    public void setAttributeValue(String attributeValue) {
        this.attributeValue = attributeValue;
    }
}
