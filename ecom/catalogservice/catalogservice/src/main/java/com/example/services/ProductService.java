package com.example.services;

import com.example.dto.Request.ProductRequest;
import com.example.dto.Response.ProductResponse;
import com.example.entities.Category;
import com.example.entities.Product;
import com.example.repository.ProductRepository;
import jakarta.inject.Singleton;

import java.util.List;
import java.util.UUID;

@Singleton
public class ProductService {
    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository){
        this.productRepository = productRepository;
    }

    public ProductResponse createProduct(ProductRequest request){
        Product product = new Product();
        product.setId(UUID.randomUUID().toString().substring(0, 10));
        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setImageUrl(request.getImageUrl());
        product.setPrice(request.getPrice());

        Category category = new Category();
        category.setId(request.getCategory_id());
        product.setCategory(category);

        Product product1 = productRepository.save(product);

        return mapToResponse(product1);
    }
    public List<ProductResponse> getByIdCate(String idCate){
            List<Product> products;

            if(idCate != null && !idCate.isEmpty()){
                products = productRepository.findByCategoryId(idCate);
            }else{
                products = productRepository.findAll();
            }

            return products.stream().map(this::mapToResponse).toList();
    }

//    public List<ProductResponse> getAllProduct(){
//        List<Product> products = productRepository.findAll();
//        return products.stream().map(this::mapToResponse).toList();
//    }
//    public Page<ProductResponse> getAllProductPaged(int page, int size) {
//        Pageable pageable = Pageable.from(page, size);
//
//        Page<Product> products = productRepository.findAll(pageable);
//
//        return products.map(this::mapToResponse);
//    }

    public ProductResponse findById(String id){
        Product product = productRepository.findById(id).orElseThrow(()->new RuntimeException("errors"));
        return mapToResponse(product);
    }

    public void deleteProducr(String id){
        productRepository.deleteById(id);
    }


    public ProductResponse mapToResponse(Product product){
        ProductResponse productResponse = new ProductResponse();
        productResponse.setId(product.getId());
        productResponse.setName(product.getName());
        productResponse.setDescription(product.getDescription());
        productResponse.setPrice(product.getPrice());
        productResponse.setImageUrl(product.getImageUrl());

        productResponse.setCategory_id(product.getCategory().getId());

        return productResponse;
    }
}
