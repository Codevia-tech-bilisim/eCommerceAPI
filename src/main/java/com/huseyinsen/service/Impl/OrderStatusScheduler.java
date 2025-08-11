package com.huseyinsen.service.Impl;

import com.huseyinsen.entity.Order;
import com.huseyinsen.entity.OrderStatus;
import com.huseyinsen.repository.OrderRepository;
import com.huseyinsen.service.Impl.OrderStateMachine;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class OrderStatusScheduler {

    private final OrderRepository orderRepository;
    private final OrderStateMachine stateMachine;

    // Her 10 dakikada bir çalışır (örnek)
    @Scheduled(fixedDelayString = "${orders.scheduler.delay-ms:600000}")
    public void autoAdvanceOrders() {
        // CONFIRMED -> PROCESSING (örnek: 1 saat içinde)
        List<Order> confirmed = orderRepository.findByStatus(OrderStatus.CONFIRMED);
        for (Order o : confirmed) {
            if (shouldAdvance(o, Duration.ofHours(1))) {
                try {
                    stateMachine.changeStatus(o.getId(), OrderStatus.PROCESSING, "system");
                } catch (Exception e) {
                    log.warn("Auto-advance failed for order {}: {}", o.getId(), e.getMessage());
                }
            }
        }

        // PROCESSING -> SHIPPED (örnek: 24 saat içinde)
        List<Order> processing = orderRepository.findByStatus(OrderStatus.PROCESSING);
        for (Order o : processing) {
            if (shouldAdvance(o, Duration.ofHours(24))) {
                try {
                    stateMachine.changeStatus(o.getId(), OrderStatus.SHIPPED, "system");
                } catch (Exception e) {
                    log.warn("Auto-advance failed for order {}: {}", o.getId(), e.getMessage());
                }
            }
        }
    }

    private boolean shouldAdvance(Order order, Duration threshold) {
        // Basit kontrol: order.getUpdatedAt() veya history timestamps'e bakılabilir
        LocalDateTime lastUpdate = order.getUpdatedAt(); // entity'de lastUpdated/updatedAt olmalı
        if (lastUpdate == null) return false;
        return Duration.between(lastUpdate, LocalDateTime.now()).compareTo(threshold) >= 0;
    }
}