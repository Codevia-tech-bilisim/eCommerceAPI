package com.huseyinsen.integration;

@SpringBootTest
@Transactional
public class TransactionRollbackTest {

    @Autowired
    private OrderService orderService;

    @Test
    public void testRollbackOnException() {
        try {
            orderService.createOrderWithException(...); // bu metot hata fırlatıyor
        } catch (Exception ignored) {}

        // Veri tabanında ilgili kayıt olmamalı, assert ile kontrol edilir
    }
}