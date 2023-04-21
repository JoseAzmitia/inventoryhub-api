package com.azmitia.inventoryhub100.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SummaryDTO {
    private int totalProducts;
    private int totalOrders;
    private int totalCategories;
    private double totalSales;
    private double totalStock;
}
