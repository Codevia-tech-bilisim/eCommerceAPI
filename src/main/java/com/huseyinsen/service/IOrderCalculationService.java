package com.huseyinsen.service;

import com.huseyinsen.dto.CreateOrderRequest;

public interface IOrderCalculationService {
    double calculateSubtotal(CreateOrderRequest request);
    double calculateTax(double subtotal);
    double calculateShippingCost(double subtotal);
    double calculateDiscount(CreateOrderRequest request);
    double calculateTotal(CreateOrderRequest request);
}