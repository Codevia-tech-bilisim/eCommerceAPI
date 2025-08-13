package com.huseyinsen.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class ProcessPaymentRequest {
    private Long orderId;
    private BigDecimal amount;
    private String method;      // Ödeme yöntemi (CREDIT_CARD, PAYPAL, vb.)
    private String cardToken;   // PSP token
}