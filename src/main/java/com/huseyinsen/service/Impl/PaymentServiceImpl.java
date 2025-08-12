package com.huseyinsen.service.Impl;

import com.huseyinsen.entity.Payment;
import com.huseyinsen.entity.PaymentAuditLog;
import com.huseyinsen.entity.PaymentMethod;
import com.huseyinsen.entity.PaymentStatus;
import com.huseyinsen.gateway.MockPaymentGateway;
import com.huseyinsen.repository.PaymentAuditRepository;
import com.huseyinsen.service.IPaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements IPaymentService {

    private final MockPaymentGateway paymentGateway;
    private final Map<Long, Payment> payments = new HashMap<>();
    private Long idCounter = 1L;

    @Autowired
    private PaymentAuditRepository paymentAuditRepository;


    @Override
    public Payment processPayment(Long orderId, BigDecimal amount, PaymentMethod method) {
        String transactionId = paymentGateway.processPayment(amount, method);

        Payment payment = Payment.builder()
                .id(idCounter++)
                .orderId(orderId)
                .amount(amount)
                .method(method)
                .status(PaymentStatus.SUCCESS)
                .transactionId(transactionId)
                .build();

        payments.put(payment.getId(), payment);
        return payment;
    }

    @Override
    public Payment getPaymentStatus(Long paymentId) {
        return payments.get(paymentId);
    }

    @Override
    public Payment refundPayment(Long paymentId) {
        Payment payment = payments.get(paymentId);
        if (payment != null && payment.getStatus() == PaymentStatus.SUCCESS) {
            paymentGateway.refundPayment(payment.getTransactionId());
            payment.setStatus(PaymentStatus.REFUNDED);
        }
        return payment;
    }

    private void logPaymentAction(Long paymentId, String action, String details) {
        PaymentAuditLog log = PaymentAuditLog.builder()
                .paymentId(paymentId)
                .action(action)
                .details(details)
                .timestamp(LocalDateTime.now())
                .build();
        paymentAuditRepository.save(log);
}