package com.huseyinsen.service.Impl;

import com.huseyinsen.entity.*;
import com.huseyinsen.repository.OrderRepository;
import com.huseyinsen.repository.OrderStatusHistoryRepository;
import com.huseyinsen.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class OrderStateMachine {

    private final OrderRepository orderRepository;
    private final OrderStatusHistoryRepository historyRepository;
    private final NotificationService notificationService;

    // İzin verilen geçişleri tanımla
    private static final Map<OrderStatus, Set<OrderStatus>> ALLOWED_TRANSITIONS = new EnumMap<>(OrderStatus.class);
    static {
        ALLOWED_TRANSITIONS.put(OrderStatus.PENDING, Set.of(OrderStatus.CONFIRMED, OrderStatus.CANCELLED));
        ALLOWED_TRANSITIONS.put(OrderStatus.CONFIRMED, Set.of(OrderStatus.PROCESSING, OrderStatus.CANCELLED));
        ALLOWED_TRANSITIONS.put(OrderStatus.PROCESSING, Set.of(OrderStatus.SHIPPED, OrderStatus.CANCELLED));
        ALLOWED_TRANSITIONS.put(OrderStatus.SHIPPED, Set.of(OrderStatus.DELIVERED));
        ALLOWED_TRANSITIONS.put(OrderStatus.DELIVERED, Set.of()); // son durum
        ALLOWED_TRANSITIONS.put(OrderStatus.CANCELLED, Set.of()); // son durum
    }

    public boolean canTransition(OrderStatus from, OrderStatus to) {
        return ALLOWED_TRANSITIONS.getOrDefault(from, Collections.emptySet()).contains(to);
    }

    @Transactional
    public Order changeStatus(Long orderId, OrderStatus newStatus, String changedBy) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Order not found: " + orderId));

        OrderStatus current = order.getStatus();
        if (current == newStatus) {
            return order; // no-op
        }

        if (!canTransition(current, newStatus)) {
            throw new IllegalStateException("Cannot change order status from " + current + " to " + newStatus);
        }

        // Değiştirme
        order.setStatus(newStatus);
        orderRepository.save(order);

        // History kaydet
        OrderStatusHistory hist = OrderStatusHistory.builder()
                .orderId(order.getId())
                .fromStatus(current)
                .toStatus(newStatus)
                .changedBy(changedBy)
                .changedAt(LocalDateTime.now())
                .build();
        historyRepository.save(hist);

        // Bildirim
        notificationService.notifyOrderStatusChanged(order.getId(),
                "Status changed from " + current + " to " + newStatus + " by " + changedBy);

        return order;
    }

    public List<OrderStatusHistory> getHistory(Long orderId) {
        return historyRepository.findByOrderIdOrderByChangedAtDesc(orderId);
    }
}