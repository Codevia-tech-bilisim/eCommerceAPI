package com.huseyinsen.controller;

import com.huseyinsen.entity.Payment;
import com.huseyinsen.entity.PaymentMethod;
import com.huseyinsen.service.IPaymentService;
import com.huseyinsen.service.Impl.PaymentServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final IPaymentService paymentService;

    @PostMapping("/process")
    public ResponseEntity<Payment> process(@RequestBody ProcessPaymentRequest req) {
        if (req.getOrderId() == null || req.getOrderId() <= 0) {
            throw new IllegalArgumentException("Geçerli bir sipariş ID'si giriniz.");
        }

        if (req.getAmount() == null || req.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Tutar sıfırdan büyük olmalıdır.");
        }

        if (req.getMethod() == null) {
            throw new IllegalArgumentException("Ödeme yöntemi seçilmelidir.");
        }

        // Eğer PSP token varsa kontrol edilebilir
        if (req.getCardToken() == null || req.getCardToken().isBlank()) {
            throw new IllegalArgumentException("Kart tokeni (cardToken) zorunludur.");
        }

        Payment payment = paymentService.processPayment(req);
        return ResponseEntity.ok(payment);
    }

    @GetMapping("/{id}/status")
    public Payment getPaymentStatus(@PathVariable Long id) {
        return paymentService.getPaymentStatus(id);
    }

    @PostMapping("/{id}/refund")
    public Payment refundPayment(@PathVariable Long id) {
        return paymentService.refundPayment(id);
    }
}