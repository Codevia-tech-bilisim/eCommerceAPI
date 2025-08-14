package com.huseyinsen.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateOrderRequest {
    @Schema(description = "Kargo adresi", example = "Atatürk Cad. No:1 İstanbul")
    private String shippingAddress;

    @Schema(description = "Fatura adresi", example = "Atatürk Cad. No:1 İstanbul")
    private String billingAddress;

    @Schema(description = "Ödeme yöntemi", example = "CREDIT_CARD")
    private String paymentMethod;

    @Schema(description = "Sipariş ürünleri listesi")
    private List<OrderItemRequest> items;
}