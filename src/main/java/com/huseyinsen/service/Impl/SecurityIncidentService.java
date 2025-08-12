package com.huseyinsen.service.Impl;

public class SecurityIncidentService {
    public void report(String title, String details) {
        // burada log + slack/webhook çağrısı veya alerting sistemine push
        log.error("SECURITY INCIDENT: {} - {}", title, details);
    }
}
