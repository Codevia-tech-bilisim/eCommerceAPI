package com.huseyinsen.service;

import com.huseyinsen.service.Impl.OrderEventPublisher;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderSagaService {

    private final OrderEventPublisher orderEventPublisher;

    public void createOrder(Long orderId, Long userId) {
        try {
            orderEventPublisher.publishOrderCreated(orderId, userId);
        } catch (Exception e) {
        }
    }
}