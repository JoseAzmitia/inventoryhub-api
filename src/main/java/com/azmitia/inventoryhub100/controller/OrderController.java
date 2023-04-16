package com.azmitia.inventoryhub100.controller;

import com.azmitia.inventoryhub100.dto.OrderDTO;
import com.azmitia.inventoryhub100.dto.ProductDTO;
import com.azmitia.inventoryhub100.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/orders")
public class OrderController {

    @Autowired
    private OrderService service;

    @PostMapping("/{userId}")
    public ResponseEntity<OrderDTO> createOrder(@PathVariable String userId, @RequestBody List<ProductDTO> products) {
        OrderDTO order = service.createOrder(userId, products);
        if (order == null) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(order);
    }

    @DeleteMapping("/{orderId}")
    public ResponseEntity<Void> deleteOrder(@PathVariable String orderId) {
        service.deleteOrder(orderId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{userId}")
    public ResponseEntity<List<OrderDTO>> getOrdersByUserId(@PathVariable String userId) {
        List<OrderDTO> orders = service.getOrdersByUserId(userId);
        return ResponseEntity.ok().body(orders);
    }

}
