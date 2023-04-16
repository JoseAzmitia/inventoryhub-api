package com.azmitia.inventoryhub100.service;

import com.azmitia.inventoryhub100.dto.OrderDTO;
import com.azmitia.inventoryhub100.dto.ProductDTO;

import java.util.List;

public interface OrderService {
    OrderDTO createOrder(String userId, List<ProductDTO> products);
    void deleteOrder(String orderId);
    List<OrderDTO> getOrdersByUserId(String userId);
}
