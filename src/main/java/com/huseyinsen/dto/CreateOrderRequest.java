package com.huseyinsen.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateOrderRequest {
    private String shippingAddress;  // adres metni ya da JSON string
    private String billingAddress;
    private String paymentMethod;
    private List<OrderItemRequest> items;
}