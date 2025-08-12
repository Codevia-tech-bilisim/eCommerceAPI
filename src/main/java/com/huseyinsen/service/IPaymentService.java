package com.huseyinsen.service;


import com.huseyinsen.entity.Payment;
import com.huseyinsen.entity.PaymentMethod;
import com.huseyinsen.entity.User;

import java.math.BigDecimal;

public interface IPaymentService {
    Payment processPayment(Long orderId, BigDecimal amount, PaymentMethod method);
    Payment getPaymentStatus(Long paymentId);
    Payment refundPayment(Long paymentId);
}