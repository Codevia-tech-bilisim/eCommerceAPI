package com.huseyinsen.service.Impl;

import com.huseyinsen.service.NotificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class LogNotificationService implements NotificationService {
    @Override
    public void notifyOrderStatusChanged(Long orderId, String message) {
        // Gerçek projede buraya email/Slack/webhook entegrasyonu koy.
        log.info("Order {}: {}", orderId, message);
    }
}