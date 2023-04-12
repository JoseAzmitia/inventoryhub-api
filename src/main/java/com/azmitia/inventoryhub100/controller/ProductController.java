package com.azmitia.inventoryhub100.controller;

import com.azmitia.inventoryhub100.dto.ProductDTO;
import com.azmitia.inventoryhub100.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/products")
public class ProductController {

    @Autowired
    private ProductService service;

    @PostMapping(value = "/create")
    public ResponseEntity<ProductDTO> createProduct(@RequestBody ProductDTO product) {
        ProductDTO createdProduct = service.createProduct(product);
        if (createdProduct != null) {
            return ResponseEntity.status(HttpStatus.CREATED).body(createdProduct);
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductDTO> updateProduct(@PathVariable String id, @RequestBody ProductDTO product) {
        ProductDTO updatedProduct = service.updateProduct(id, product);
        if (updatedProduct != null) {
            return ResponseEntity.ok(updatedProduct);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable String id) {
        try {
            service.deleteProduct(id);
            return ResponseEntity.ok("Product with id " + id + " deleted successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error deleting product with id " + id);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDTO> getProductById(@PathVariable String id) {
        ProductDTO product = service.getProductById(id);
        if (product != null) {
            return ResponseEntity.ok(product);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<ProductDTO>> getAllProductsByUser(@PathVariable String userId) {
        List<ProductDTO> products = service.getAllProductsByUser(userId);
        if (products.isEmpty()) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(products);
        }
    }
}
