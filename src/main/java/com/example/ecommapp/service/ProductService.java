package com.example.ecommapp.service;

import com.example.ecommapp.dto.ProductRequest;
import com.example.ecommapp.model.Product;
import com.example.ecommapp.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    // admin adds a product
    public Product addProduct(ProductRequest request) {
        Product product = new Product();
        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setPrice(request.getPrice());
        product.setStockQuantity(request.getStockQuantity());
        product.setCategory(request.getCategory());
        product.setImageUrl(request.getImageUrl());
        product.setActive(true);
        product.setCreatedAt(LocalDateTime.now());
        return productRepository.save(product);
    }

    // anyone can view all active products
    public List<Product> getAllActiveProducts() {
        return productRepository.findByActiveTrue();
    }

    // get single product by id
    public Product getProductById(String id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));
    }

    // filter by category
    public List<Product> getByCategory(String category) {
        return productRepository.findByCategoryAndActiveTrue(category);
    }

    // search by name
    public List<Product> searchByName(String name) {
        return productRepository.findByNameContainingIgnoreCase(name);
    }

    // admin updates a product
    public Product updateProduct(String id, ProductRequest request) {
        Product product = getProductById(id);
        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setPrice(request.getPrice());
        product.setStockQuantity(request.getStockQuantity());
        product.setCategory(request.getCategory());
        product.setImageUrl(request.getImageUrl());
        return productRepository.save(product);
    }

    // soft delete — never hard delete products
    public String deleteProduct(String id) {
        Product product = getProductById(id);
        product.setActive(false);
        productRepository.save(product);
        return "Product deleted successfully";
    }
}