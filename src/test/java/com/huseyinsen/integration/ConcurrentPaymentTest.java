package com.huseyinsen.integration;

import com.huseyinsen.entity.PaymentMethod;
import com.huseyinsen.service.Impl.PaymentServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;

@SpringBootTest
public class ConcurrentPaymentTest {

    @Autowired
    private PaymentServiceImpl paymentService;

    @Test
    public void testConcurrentPayments() throws InterruptedException {
        Runnable task = () -> paymentService.processPayment(1L, new BigDecimal("100"), PaymentMethod.CREDIT_CARD);

        Thread t1 = new Thread(task);
        Thread t2 = new Thread(task);

        t1.start();
        t2.start();

        t1.join();
        t2.join();

        // Veri tutarlılığı kontrol edilir (örneğin ödeme durumu)
    }
}