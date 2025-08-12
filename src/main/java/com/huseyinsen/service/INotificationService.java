package com.huseyinsen.service;

public interface INotificationService {
    void sendEmail(String to, String subject, String templateName, Object model);
    void sendSms(String phoneNumber, String message);
    void sendPush(String deviceToken, String title, String message);
}