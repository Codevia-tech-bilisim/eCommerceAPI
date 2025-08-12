package com.huseyinsen.gateway;

import com.huseyinsen.entity.Payment;
import com.huseyinsen.entity.PaymentMethod;
import com.huseyinsen.entity.PaymentStatus;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.UUID;

@Component
public class MockPaymentGateway implements PaymentGateway {

    public String processPayment(BigDecimal amount, PaymentMethod method) {
        System.out.println("Processing payment: " + amount + " via " + method);
        return "TXN-" + System.currentTimeMillis();
    }
    @Override
    public void refundPayment(String transactionId) {
        System.out.println("Refund processed for transaction: " + transactionId);
    }

}