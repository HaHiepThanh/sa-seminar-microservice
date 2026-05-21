package com.example.services;

import com.example.dto.Request.ProductAttributesRequest;
import com.example.dto.Response.ProductAttributesResponse;
import com.example.entities.Product;
import com.example.entities.ProductAttributes;
import com.example.repository.ProductAttributesRepository;
import jakarta.inject.Singleton;

import java.util.List;
import java.util.UUID;

@Singleton
public class ProductAttributesService {
    private final ProductAttributesRepository productAttributesRepository;

    public ProductAttributesService(ProductAttributesRepository productAttributesRepository) {
        this.productAttributesRepository = productAttributesRepository;
    }

    public ProductAttributesResponse createProductAttri(ProductAttributesRequest request){
        ProductAttributes attributes = new ProductAttributes();
        attributes.setId(UUID.randomUUID().toString());
        attributes.setAttribute_name(request.getAttributeName());
        attributes.setAttribute_type(request.getAttributeType());
        attributes.setAttribute_value(request.getAttributeValue());

        Product product = new Product();
        product.setId(request.getIdProduct());

        attributes.setProduct(product);

        ProductAttributes productAttributes = productAttributesRepository.save(attributes);
        return mapToResponse(productAttributes);
    }

    public List<ProductAttributesResponse> getAllAttribute(){
        var att = productAttributesRepository.findAll();
        return att.stream().map(this::mapToResponse).toList();
    }

    public ProductAttributesResponse findById(String id){
        ProductAttributes attributes = productAttributesRepository.findById(id).orElseThrow(()->new RuntimeException("errors"));
        return mapToResponse(attributes);
    }

    public void deleteById(String id){
        productAttributesRepository.deleteById(id);
    }


    public ProductAttributesResponse mapToResponse(ProductAttributes attributes){
       ProductAttributesResponse product = new ProductAttributesResponse();
       product.setId(attributes.getId());
       product.setAttributeName(attributes.getAttribute_name());
       product.setAttributeType(attributes.getAttribute_type());
       product.setAttributeValue(attributes.getAttribute_value());
       product.setIdProduct(attributes.getProduct().getId());
       return product;
    }
}
