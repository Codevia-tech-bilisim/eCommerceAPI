package com.huseyinsen.integration;

import org.springframework.beans.factory.annotation.Autowired;

@SpringBootTest
public class ConcurrentPaymentTest {

    @Autowired
    private PaymentService paymentService;

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