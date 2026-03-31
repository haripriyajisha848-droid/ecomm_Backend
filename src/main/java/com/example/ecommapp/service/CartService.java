package com.example.ecommapp.service;

import com.example.ecommapp.model.Cart;
import com.example.ecommapp.model.CartItem;
import com.example.ecommapp.model.Product;
import com.example.ecommapp.repository.CartRepository;
import com.example.ecommapp.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;
    private final ProductRepository productRepository;

    // get cart for a user — create one if doesn't exist
    public Cart getCart(String userId) {
        return cartRepository.findByUserId(userId)
                .orElseGet(() -> {
                    Cart newCart = new Cart();
                    newCart.setUserId(userId);
                    newCart.setItems(new ArrayList<>());
                    newCart.setTotalAmount(0.0);
                    return cartRepository.save(newCart);
                });
    }

    // add item to cart
    public Cart addToCart(String userId, String productId, int quantity) {

        // check product exists and has enough stock
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        if (product.getStockQuantity() < quantity) {
            throw new RuntimeException("Insufficient stock. Available: "
                    + product.getStockQuantity());
        }

        Cart cart = getCart(userId);

        // check if product already in cart
        Optional<CartItem> existingItem = cart.getItems().stream()
                .filter(item -> item.getProductId().equals(productId))
                .findFirst();

        if (existingItem.isPresent()) {
            // just increase quantity
            existingItem.get().setQuantity(
                    existingItem.get().getQuantity() + quantity
            );
        } else {
            // add new item
            CartItem newItem = new CartItem();
            newItem.setProductId(productId);
            newItem.setProductName(product.getName());
            newItem.setPrice(product.getPrice());
            newItem.setQuantity(quantity);
            cart.getItems().add(newItem);
        }

        // recalculate total
        recalculateTotal(cart);
        return cartRepository.save(cart);
    }

    // remove item from cart
    public Cart removeFromCart(String userId, String productId) {
        Cart cart = getCart(userId);
        cart.getItems().removeIf(item -> item.getProductId().equals(productId));
        recalculateTotal(cart);
        return cartRepository.save(cart);
    }

    // update quantity of an item
    public Cart updateQuantity(String userId, String productId, int quantity) {
        if (quantity <= 0) {
            return removeFromCart(userId, productId);
        }

        Cart cart = getCart(userId);
        cart.getItems().stream()
                .filter(item -> item.getProductId().equals(productId))
                .findFirst()
                .ifPresent(item -> item.setQuantity(quantity));

        recalculateTotal(cart);
        return cartRepository.save(cart);
    }

    // clear entire cart
    public void clearCart(String userId) {
        Cart cart = getCart(userId);
        cart.getItems().clear();
        cart.setTotalAmount(0.0);
        cartRepository.save(cart);
    }

    // helper — recalculates total from all items
    private void recalculateTotal(Cart cart) {
        double total = cart.getItems().stream()
                .mapToDouble(item -> item.getPrice() * item.getQuantity())
                .sum();
        cart.setTotalAmount(total);
    }
}