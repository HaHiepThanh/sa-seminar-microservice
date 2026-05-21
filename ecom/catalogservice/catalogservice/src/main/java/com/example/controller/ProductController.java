package com.example.controller;


import com.example.dto.Request.ProductRequest;
import com.example.dto.Response.ProductResponse;
import com.example.services.ProductService;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.data.model.Page;
import io.micronaut.http.annotation.*;
import io.micronaut.scheduling.TaskExecutors;
import io.micronaut.scheduling.annotation.ExecuteOn;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecurityRule;

import java.util.List;

@ExecuteOn(TaskExecutors.IO)
@Secured(SecurityRule.IS_ANONYMOUS)
@Controller("/api/products")
public class ProductController {
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @Post
    public ProductResponse createProduct(@Body ProductRequest request){
        ProductResponse productResponse = productService.createProduct(request);
        String msg = "vua moi them san pham moi: " + productResponse.getName();

        return productResponse;
    }

//    @Get
//    public Page<ProductResponse> getAllProduct(
//            @QueryValue(defaultValue = "0") int page,
//            @QueryValue(defaultValue = "10") int size) {
//        return productService.getAllProductPaged(page, size);
//    }

    @Get
    public List<ProductResponse> getAll(@Nullable @QueryValue String categoryId) {
        return productService.getByIdCate(categoryId);
    }

    @Get("{id}")
    public ProductResponse getById(@PathVariable String id){
        return productService.findById(id);
    }

    @Delete("{id}")
    public void deleteById(@PathVariable String id){
        productService.deleteProducr(id);
    }
}
