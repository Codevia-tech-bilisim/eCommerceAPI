package com.huseyinsen.entity;

import lombok.Data;

@Data
public class NotificationPreference {
    private boolean emailEnabled;
    private boolean smsEnabled;
    private boolean pushEnabled;
}