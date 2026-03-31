package com.example.ecommapp.controller;

import com.example.ecommapp.model.Cart;
import com.example.ecommapp.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    // Authentication object gives us the logged-in user's email from JWT
    @GetMapping
    public ResponseEntity<Cart> getCart(Authentication authentication) {
        String userId = authentication.getName(); // this is the email from JWT
        return ResponseEntity.ok(cartService.getCart(userId));
    }

    @PostMapping("/add")
    public ResponseEntity<Cart> addToCart(
            @RequestParam String productId,
            @RequestParam int quantity,
            Authentication authentication) {
        String userId = authentication.getName();
        return ResponseEntity.ok(cartService.addToCart(userId, productId, quantity));
    }

    @DeleteMapping("/remove/{productId}")
    public ResponseEntity<Cart> removeFromCart(
            @PathVariable String productId,
            Authentication authentication) {
        String userId = authentication.getName();
        return ResponseEntity.ok(cartService.removeFromCart(userId, productId));
    }

    @PutMapping("/update")
    public ResponseEntity<Cart> updateQuantity(
            @RequestParam String productId,
            @RequestParam int quantity,
            Authentication authentication) {
        String userId = authentication.getName();
        return ResponseEntity.ok(cartService.updateQuantity(userId, productId, quantity));
    }
}