package com.huseyinsen.service;


public interface NotificationService {
    void notifyOrderStatusChanged(Long orderId, String message);
}