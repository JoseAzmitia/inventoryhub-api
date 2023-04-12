package com.azmitia.inventoryhub100.service;

import com.azmitia.inventoryhub100.dto.ProductDTO;

import java.util.List;

public interface ProductService {
    List<ProductDTO> getAllProductsByUser(String userId);
    ProductDTO getProductById(String id);
    ProductDTO createProduct(ProductDTO product);
    ProductDTO updateProduct(String id, ProductDTO product);
    void deleteProduct(String id);
}
