package com.huseyinsen.service.Impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class SecurityIncidentService {
    public void report(String title, String details) {
        log.error("SECURITY INCIDENT: {} - {}", title, details);
    }
}