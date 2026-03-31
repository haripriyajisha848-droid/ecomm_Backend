package com.example.ecommapp.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class ProductRequest {

    @NotBlank(message = "Product name is required")
    private String name;

    private String description;

    @Positive(message = "Price must be greater than 0")
    private double price;

    @Positive(message = "Stock must be greater than 0")
    private int stockQuantity;

    @NotBlank(message = "Category is required")
    private String category;

    private String imageUrl;
}