package com.huseyinsen.gateway;

import com.huseyinsen.entity.Payment;
import com.huseyinsen.entity.PaymentMethod;
import com.huseyinsen.entity.PaymentStatus;
import com.huseyinsen.entity.User;

import java.math.BigDecimal;

public interface PaymentGateway {
    String processPayment(BigDecimal amount, PaymentMethod method);

    void refundPayment(String transactionId);
}