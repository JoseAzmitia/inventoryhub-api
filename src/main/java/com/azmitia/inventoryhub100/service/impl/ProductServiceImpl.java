package com.azmitia.inventoryhub100.service.impl;

import com.azmitia.inventoryhub100.dto.ProductDTO;
import com.azmitia.inventoryhub100.service.ProductService;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.DocumentReference;
import firebase.FirebaseInitializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private FirebaseInitializer firebase;

    @Override
    public List<ProductDTO> getAllProductsByUser(String userId) {
        return null;
    }

    @Override
    public ProductDTO getProductById(String id) {
        return null;
    }

    @Override
    public ProductDTO  createProduct(ProductDTO product) {
        Map<String, Object> productMap = new HashMap<>();
        productMap.put("name", product.getName());
        productMap.put("category", product.getCategory());
        productMap.put("price", product.getPrice());
        productMap.put("stock", product.getStock());
        productMap.put("image", product.getImage());
        productMap.put("userId", product.getUserId());

        CollectionReference products = firebase.getFirestore().collection("product");
        ApiFuture<DocumentReference> documentReferenceApiFuture = products.add(productMap);

        try {
            if (null != documentReferenceApiFuture.get()) {
                product.setId(documentReferenceApiFuture.get().getId());
                return product;
            }
            return null;
        } catch (Exception e) {
            return null;
        }
    }
    @Override
    public ProductDTO updateProduct(String id, ProductDTO product) {
        return null;
    }

    @Override
    public void deleteProduct(String id) {

    }
}
