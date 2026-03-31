package com.example.ecommapp.repository;

import com.example.ecommapp.model.Product;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends MongoRepository<Product, String> {

    List<Product> findByCategory(String category);

    List<Product> findByNameContainingIgnoreCase(String name);

    List<Product> findByActiveTrue();

    List<Product> findByCategoryAndActiveTrue(String category);
}