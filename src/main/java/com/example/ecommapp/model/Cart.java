package com.example.ecommapp.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "cart")
public class Cart {

    @Id
    private String id;

    private String userId;   // links to User._id

    private List<CartItem> items = new ArrayList<>();

    private double totalAmount;
}