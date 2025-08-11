package com.huseyinsen.service;

import com.huseyinsen.dto.CreateOrderRequest;
import com.huseyinsen.entity.User;

public interface IOrderValidationService {
    void validateMinimumOrderAmount(double totalAmount);
    void validateMaxQuantityPerItem(CreateOrderRequest request);
    void validateProductAvailability(CreateOrderRequest request);
    void validateUserAddress(User user, Long addressId);
    void validatePaymentMethod(String paymentMethod);
    void validateOrderModification(Long orderId);
}