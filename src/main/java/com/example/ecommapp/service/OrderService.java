package com.example.ecommapp.service;

import com.example.ecommapp.model.Cart;
import com.example.ecommapp.model.Order;
import com.example.ecommapp.model.Product;
import com.example.ecommapp.repository.OrderRepository;
import com.example.ecommapp.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final CartService cartService;

    // place an order from cart
    public Order placeOrder(String userId, String shippingAddress) {

        // get user's cart
        Cart cart = cartService.getCart(userId);

        if (cart.getItems().isEmpty()) {
            throw new RuntimeException("Cart is empty");
        }

        // verify stock and deduct for each item
        cart.getItems().forEach(item -> {
            Product product = productRepository.findById(item.getProductId())
                    .orElseThrow(() -> new RuntimeException(
                            "Product not found: " + item.getProductName()));

            if (product.getStockQuantity() < item.getQuantity()) {
                throw new RuntimeException(
                        "Insufficient stock for: " + product.getName()
                                + ". Available: " + product.getStockQuantity());
            }

            // deduct stock
            product.setStockQuantity(
                    product.getStockQuantity() - item.getQuantity()
            );
            productRepository.save(product);
        });

        // create order — snapshot of cart at this moment
        Order order = new Order();
        order.setUserId(userId);
        order.setItems(new ArrayList<>(cart.getItems()));
        order.setTotalAmount(cart.getTotalAmount());
        order.setStatus("PLACED");
        order.setShippingAddress(shippingAddress);
        order.setOrderedAt(LocalDateTime.now());

        Order savedOrder = orderRepository.save(order);

        // clear cart after order placed
        cartService.clearCart(userId);

        return savedOrder;
    }

    // get all orders for a user
    public List<Order> getMyOrders(String userId) {
        return orderRepository.findByUserId(userId);
    }

    // admin — get all orders
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    // admin — update order status
    public Order updateStatus(String orderId, String status) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));
        order.setStatus(status);
        return orderRepository.save(order);
    }
}