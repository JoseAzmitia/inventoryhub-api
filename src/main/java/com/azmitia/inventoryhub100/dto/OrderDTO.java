package com.azmitia.inventoryhub100.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
public class OrderDTO {
    private String id;
    private Date createdAt;
    private List<ProductDTO> products;
    private BigDecimal totalValue;
    private String userId;
}
