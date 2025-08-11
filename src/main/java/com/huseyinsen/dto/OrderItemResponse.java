package com.huseyinsen.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class OrderItemResponse {
    private String productName;
    private int quantity;
    private BigDecimal unitPrice;
}