package com.azmitia.inventoryhub100.service.impl;

import com.azmitia.inventoryhub100.dto.OrderDTO;
import com.azmitia.inventoryhub100.dto.ProductDTO;
import com.azmitia.inventoryhub100.service.OrderService;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import firebase.FirebaseInitializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.ExecutionException;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private FirebaseInitializer firebase;

    @Override
    public OrderDTO createOrder(String userId, List<ProductDTO> products) {
        // Obtener la referencia a la colección de productos
        CollectionReference productsCollection = firebase.getFirestore().collection("product");

        for (ProductDTO product : products) {
            DocumentReference productDoc = productsCollection.document(product.getId());
            // Obtener el stock actual del producto
            ApiFuture<DocumentSnapshot> future = productDoc.get();
            try {
                DocumentSnapshot document = future.get();
                if (document.exists()) {
                    ProductDTO existingProduct = document.toObject(ProductDTO.class);
                    // Actualizar el stock
                    int newStock = existingProduct.getStock() - product.getQuantity();
                    productDoc.update("stock", newStock);
                }
            } catch (InterruptedException | ExecutionException e) {
                System.err.println("Error actualizando el stock del producto " + product.getId());
            }
        }

        Map<String, Object> orderMap = new HashMap<>();
        orderMap.put("userId", userId);
        orderMap.put("products", products);
        orderMap.put("totalValue", calculateTotalValue(products));
        orderMap.put("createdAt", new Date());

        CollectionReference orders = getCollection();
        ApiFuture<DocumentReference> documentReferenceApiFuture = orders.add(orderMap);

        try {
            if (null != documentReferenceApiFuture.get()) {
                OrderDTO order = new OrderDTO();
                order.setId(documentReferenceApiFuture.get().getId());
                order.setUserId(userId);
                order.setProducts(products);
                order.setTotalValue(calculateTotalValue(products));
                order.setCreatedAt(new Date());
                return order;
            }
            return null;
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public void deleteOrder(String orderId) {
        // Obtener la referencia a la colección de productos
        CollectionReference productsCollection = firebase.getFirestore().collection("product");

        CollectionReference orders = getCollection();
        DocumentReference orderDoc = orders.document(orderId);

        ApiFuture<DocumentSnapshot> orderFuture = orderDoc.get();
        try {
            DocumentSnapshot orderDocument = orderFuture.get();
            if (orderDocument.exists()) {
                OrderDTO order = orderDocument.toObject(OrderDTO.class);

                // Recorrer los productos de la orden y actualizar el stock
                for (ProductDTO product : order.getProducts()) {
                    DocumentReference productDoc = productsCollection.document(product.getId());

                    ApiFuture<DocumentSnapshot> productFuture = productDoc.get();
                    DocumentSnapshot productDocument = productFuture.get();

                    if (productDocument.exists()) {
                        ProductDTO existingProduct = productDocument.toObject(ProductDTO.class);

                        // Sumar la cantidad de la orden al stock del producto
                        int newStock = existingProduct.getStock() + product.getQuantity();
                        productDoc.update("stock", newStock);
                    }
                }

                // Borrar la orden
                orderDoc.delete();
            }
        } catch (InterruptedException | ExecutionException e) {
            System.err.println("Error al cancelar la orden " + orderId);
        }
    }

    @Override
    public List<OrderDTO> getOrdersByUserId(String userId) {
        CollectionReference orders = getCollection();
        Query query = orders.whereEqualTo("userId", userId);
        ApiFuture<QuerySnapshot> querySnapshotApiFuture = query.get();
        List<OrderDTO> orderList = new ArrayList<>();

        try {
            for (QueryDocumentSnapshot document : querySnapshotApiFuture.get().getDocuments()) {
                OrderDTO order = document.toObject(OrderDTO.class);
                order.setId(document.getId());
                orderList.add(order);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return orderList;
    }

    private CollectionReference getCollection() {
        return firebase.getFirestore().collection("order");
    }

    private BigDecimal calculateTotalValue(List<ProductDTO> products) {
        double totalValue = 0.0;
        for (ProductDTO product : products) {
            totalValue += product.getPrice() * product.getQuantity();
        }
        return BigDecimal.valueOf(totalValue);
    }
}
