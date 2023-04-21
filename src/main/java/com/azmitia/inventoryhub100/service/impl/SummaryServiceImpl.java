package com.azmitia.inventoryhub100.service.impl;

import com.azmitia.inventoryhub100.dto.ProductDTO;
import com.azmitia.inventoryhub100.dto.SummaryDTO;
import com.azmitia.inventoryhub100.service.SummaryService;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import firebase.FirebaseInitializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutionException;

@Service
public class SummaryServiceImpl implements SummaryService {

    @Autowired
    private FirebaseInitializer firebase;

    @Override
    public SummaryDTO getSummaryByUserId(String userId) {
        SummaryDTO summaryDTO = new SummaryDTO();

        try {
            Firestore firestore = firebase.getFirestore();
            CollectionReference products = firestore.collection("product");
            CollectionReference orders = firestore.collection("order");

            // Obtener el total de productos
            Query productQuery = products.whereEqualTo("userId", userId);
            ApiFuture<QuerySnapshot> productQuerySnapshot = productQuery.get();
            int totalProducts = productQuerySnapshot.get().size();
            summaryDTO.setTotalProducts(totalProducts);

            // Obtener el total de órdenes
            Query orderQuery = orders.whereEqualTo("userId", userId);
            ApiFuture<QuerySnapshot> orderQuerySnapshot = orderQuery.get();
            int totalOrders = orderQuerySnapshot.get().size();
            summaryDTO.setTotalOrders(totalOrders);

            // Obtener el total de ventas
            double totalSales = 0;
            for (QueryDocumentSnapshot orderDocument : orderQuerySnapshot.get().getDocuments()) {
                totalSales += Double.parseDouble(orderDocument.getString("totalValue"));
            }
            summaryDTO.setTotalSales(totalSales);

            // Obtener el total de categorías
            Query categoryQuery = products.whereEqualTo("userId", userId).select("category");
            ApiFuture<QuerySnapshot> categoryQuerySnapshot = categoryQuery.get();
            Set<String> categories = new HashSet<>();
            for (QueryDocumentSnapshot productDocument : categoryQuerySnapshot.get().getDocuments()) {
                categories.add(productDocument.getString("category"));
            }
            int totalCategories = categories.size();
            summaryDTO.setTotalCategories(totalCategories);

            // Obtener el total de inventario
            int totalStock = 0;
            for (QueryDocumentSnapshot productDocument : productQuerySnapshot.get().getDocuments()) {
                totalStock += productDocument.getLong("stock").intValue();
            }
            summaryDTO.setTotalStock(totalStock);

        } catch (InterruptedException | ExecutionException e) {
            System.out.println(e.getMessage());
        }

        return summaryDTO;
    }
}
