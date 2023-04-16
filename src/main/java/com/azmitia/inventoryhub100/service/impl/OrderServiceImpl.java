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

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private FirebaseInitializer firebase;

    @Override
    public OrderDTO createOrder(String userId, List<ProductDTO> products) {
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
        CollectionReference orders = getCollection();
        orders.document(orderId).delete();
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
            totalValue += product.getPrice();
        }
        return BigDecimal.valueOf(totalValue);
    }
}
