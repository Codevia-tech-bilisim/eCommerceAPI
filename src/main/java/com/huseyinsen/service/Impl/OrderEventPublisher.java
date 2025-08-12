package com.huseyinsen.service.Impl;

import com.huseyinsen.event.OrderCreatedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderEventPublisher {

    private final ApplicationEventPublisher publisher;

    public void publishOrderCreated(Long orderId, Long userId) {
        publisher.publishEvent(new OrderCreatedEvent(orderId, userId));
    }
}