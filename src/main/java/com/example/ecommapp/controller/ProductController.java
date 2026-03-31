package com.example.ecommapp.controller;

import com.example.ecommapp.dto.ProductRequest;
import com.example.ecommapp.model.Product;
import com.example.ecommapp.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    // PUBLIC endpoints — no token needed
    // matches /api/products/public/** in SecurityConfig
    @GetMapping("/api/products/public/all")
    public List<Product> getAllProducts() {
        return productService.getAllActiveProducts();
    }

    @GetMapping("/api/products/public/{id}")
    public Product getProductById(@PathVariable String id) {
        return productService.getProductById(id);
    }

    @GetMapping("/api/products/public/category/{category}")
    public List<Product> getByCategory(@PathVariable String category) {
        return productService.getByCategory(category);
    }

    @GetMapping("/api/products/public/search")
    public List<Product> searchProducts(@RequestParam String name) {
        return productService.searchByName(name);
    }

    // ADMIN only endpoints — token + ROLE_ADMIN required
    @PostMapping("/api/products")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<Product> addProduct(@Valid @RequestBody ProductRequest request) {
        return ResponseEntity.ok(productService.addProduct(request));
    }

    @PutMapping("/api/products/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<Product> updateProduct(@PathVariable String id,
                                                 @Valid @RequestBody ProductRequest request) {
        return ResponseEntity.ok(productService.updateProduct(id, request));
    }

    @DeleteMapping("/api/products/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<String> deleteProduct(@PathVariable String id) {
        return ResponseEntity.ok(productService.deleteProduct(id));
    }
}