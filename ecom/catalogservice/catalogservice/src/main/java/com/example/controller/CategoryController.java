package com.example.controller;

import com.example.dto.Request.CategoryRequest;
import com.example.dto.Response.CategoryResponse;
import com.example.dto.Response.ProductAttributesResponse;
import com.example.services.CategoryService;
import io.micronaut.http.annotation.*;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecurityRule;

import java.util.List;
@Secured(SecurityRule.IS_ANONYMOUS)
@Controller("/api/categories")
public class CategoryController {
    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @Post
    public CategoryResponse createCategory(@Body CategoryRequest request){
         return categoryService.createCategory(request);
    }

    @Get
    public List<CategoryResponse> getAllRepository(){
        return categoryService.findAllCate();
    }


    @Get("{id}")
    public CategoryResponse getById(@PathVariable String id){
        return categoryService.findById(id);
    }

    @Delete("{id}")
    public void deleteById(@PathVariable String id){
        categoryService.deleteCategory(id);
    }
}
