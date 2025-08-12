package com.huseyinsen.service;

import com.huseyinsen.entity.NotificationPreference;

public interface NotificationPreferenceService {
    NotificationPreference getUserPreferences(Long userId);
    void updateUserPreferences(Long userId, NotificationPreference preference);
}