package com.azmitia.inventoryhub100.service.impl;

import com.azmitia.inventoryhub100.dto.ProductDTO;
import com.azmitia.inventoryhub100.service.ProductService;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import firebase.FirebaseInitializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private FirebaseInitializer firebase;

    @Override
    public List<ProductDTO> getAllProductsByUser(String userId) {
        CollectionReference products = getCollection();
        Query query = products.whereEqualTo("userId", userId);
        ApiFuture<QuerySnapshot> querySnapshotApiFuture = query.get();
        List<ProductDTO> productList = new ArrayList<>();

        try {
            for (QueryDocumentSnapshot document : querySnapshotApiFuture.get().getDocuments()) {
                ProductDTO product = document.toObject(ProductDTO.class);
                product.setId(document.getId());
                productList.add(product);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return productList;
    }

    @Override
    public ProductDTO getProductById(String id) {
        CollectionReference products = getCollection();
        DocumentReference document = products.document(id);
        ApiFuture<DocumentSnapshot> documentSnapshotApiFuture = document.get();

        try {
            DocumentSnapshot documentSnapshot = documentSnapshotApiFuture.get();
            if (documentSnapshot.exists()) {
                ProductDTO product = documentSnapshot.toObject(ProductDTO.class);
                product.setId(documentSnapshot.getId());
                return product;
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

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

        CollectionReference products = getCollection();
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
        CollectionReference products = getCollection();
        DocumentReference document = products.document(id);

        Map<String, Object> updates = new HashMap<>();
        addIfNotNull(updates, "name", product.getName());
        addIfNotNull(updates, "category", product.getCategory());
        addIfNotNull(updates, "price", product.getPrice());
        addIfNotNull(updates, "stock", product.getStock());
        addIfNotNull(updates, "image", product.getImage());

        try {
            document.update(updates).get();
            product.setId(id);
            return product;
        } catch (InterruptedException | ExecutionException e) {
            System.out.println(e.getMessage());
            return null;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteProduct(String id) {
        CollectionReference products = getCollection();
        products.document(id).delete();
    }

    private CollectionReference getCollection() {
        return firebase.getFirestore().collection("product");
    }

    private void addIfNotNull(Map<String, Object> map, String key, Object value) {
        if (value != null) {
            map.put(key, value);
        }
    }
}
