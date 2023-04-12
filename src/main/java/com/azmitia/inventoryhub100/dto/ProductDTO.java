package com.azmitia.inventoryhub100.dto;

import lombok.Data;

@Data
public class ProductDTO {
    private String id;
    private String name;
    private Double price;
    private Integer stock;
    private String image;
    private String category;
    private String userId;
}
