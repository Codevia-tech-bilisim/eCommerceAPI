package com.huseyinsen.integration;

import com.huseyinsen.dto.CreateOrderRequest;
import com.huseyinsen.dto.OrderItemRequest;
import com.huseyinsen.service.Impl.OrderServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import org.junit.jupiter.api.Test;
import java.util.List;

@SpringBootTest
@Transactional
public class TransactionRollbackTest {

    @Autowired
    private OrderServiceImpl orderService;

    @Test
    public void testRollbackOnException() {
        // 1. CreateOrderRequest oluştur
        CreateOrderRequest request = new CreateOrderRequest();
        request.setShippingAddress("123 Test Street, Istanbul, Turkey");
        request.setBillingAddress("123 Test Street, Istanbul, Turkey");
        request.setPaymentMethod("CREDIT_CARD");
        request.setItems(List.of(
                new OrderItemRequest(1L, 2) // productId=1, quantity=2
        ));

        String userEmail = "test@example.com";

        // 2. Exception fırlatan metod çağrısı
        try {
            orderService.createOrderWithException(request, userEmail);
        } catch (Exception ignored) {}

        // 3. Burada assert ile rollback kontrolü yapabilirsin
        // Örnek: orderRepository.findByUserEmail(userEmail) -> boş olmalı
    }
}