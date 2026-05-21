package com.example.controller;


import com.example.dto.Request.ProductAttributesRequest;
import com.example.dto.Response.ProductAttributesResponse;
import com.example.dto.Response.ProductResponse;
import com.example.services.ProductAttributesService;
import io.micronaut.http.annotation.*;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecurityRule;

import java.util.List;

@Secured(SecurityRule.IS_ANONYMOUS)
@Controller("/api/attributes")
public class ProductAttributesController {
    private final ProductAttributesService productAttributesService;

    public ProductAttributesController(ProductAttributesService productAttributesService){
        this.productAttributesService = productAttributesService;
    }

    @Post
    public ProductAttributesResponse createProductAttributes(@Body ProductAttributesRequest request){
        return productAttributesService.createProductAttri(request);
    }

    @Get
    public List<ProductAttributesResponse> getAllAttributes(){
        return productAttributesService.getAllAttribute();
    }

    @Get("{id}")
    public ProductAttributesResponse getById(@PathVariable String id){
        return productAttributesService.findById(id);
    }

    @Delete("{id}")
    public void deleteById(@PathVariable String id){
        productAttributesService.deleteById(id);
    }
}
