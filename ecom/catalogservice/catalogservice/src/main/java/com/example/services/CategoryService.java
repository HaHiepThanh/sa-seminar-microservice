package com.example.services;

import com.example.dto.Request.CategoryRequest;
import com.example.dto.Response.CategoryResponse;
import com.example.entities.Category;
import com.example.repository.CategoryRepository;
import jakarta.inject.Singleton;

import java.util.List;
import java.util.UUID;

@Singleton
public class CategoryService {
    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public CategoryResponse createCategory(CategoryRequest request){
        Category category = new Category();
        category.setId(UUID.randomUUID().toString());
        category.setName(request.getName());

        Category category1 = categoryRepository.save(category);
        return mapToResponse(category1);
    }

    public List<CategoryResponse> findAllCate(){
        var category = categoryRepository.findAll();
        return category.stream().map(this::mapToResponse).toList();
    }

    public CategoryResponse findById(String id){
        Category category = categoryRepository.findById(id).orElseThrow(()->new RuntimeException("errors"));
        return mapToResponse(category);
    }

    public void deleteCategory(String id){
        categoryRepository.deleteById(id);
    }

    public CategoryResponse mapToResponse(Category category){
        CategoryResponse categoryResponse = new CategoryResponse();
        categoryResponse.setId(category.getId());
        categoryResponse.setName(category.getName());
        return  categoryResponse;

    }
}
