package com.example.ecommapp.controller;

import com.example.ecommapp.model.Order;
import com.example.ecommapp.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    // customer places order
    @PostMapping("/place")
    public ResponseEntity<Order> placeOrder(
            @RequestParam String shippingAddress,
            Authentication authentication) {
        String userId = authentication.getName();
        return ResponseEntity.ok(orderService.placeOrder(userId, shippingAddress));
    }

    // customer sees their own orders
    @GetMapping("/my")
    public ResponseEntity<List<Order>> getMyOrders(Authentication authentication) {
        String userId = authentication.getName();
        return ResponseEntity.ok(orderService.getMyOrders(userId));
    }

    // admin sees all orders
    @GetMapping("/all")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<List<Order>> getAllOrders() {
        return ResponseEntity.ok(orderService.getAllOrders());
    }

    // admin updates order status
    @PutMapping("/{orderId}/status")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<Order> updateStatus(
            @PathVariable String orderId,
            @RequestParam String status) {
        return ResponseEntity.ok(orderService.updateStatus(orderId, status));
    }
}
