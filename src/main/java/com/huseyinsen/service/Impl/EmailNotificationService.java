package com.huseyinsen.service.impl;

import com.huseyinsen.service.INotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.internet.MimeMessage;

@Service
@RequiredArgsConstructor
public class EmailNotificationService implements INotificationService {

    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;

    @Override
    public void sendEmail(String to, String subject, String templateName, Object model) {
        try {
            Context context = new Context();
            context.setVariables((java.util.Map<String, Object>) model);

            String htmlContent = templateEngine.process(templateName, context);

            MimeMessage mimeMessage = mailSender.createMimeMessage();
            javax.mail.internet.MimeMessageHelper helper = new javax.mail.internet.MimeMessageHelper(mimeMessage, true);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(htmlContent, true);

            mailSender.send(mimeMessage);
        } catch (Exception e) {
            throw new RuntimeException("Email gönderilemedi", e);
        }
    }

    @Override
    public void sendSms(String phoneNumber, String message) {
        System.out.println("SMS gönderildi -> " + phoneNumber + ": " + message);
    }

    @Override
    public void sendPush(String deviceToken, String title, String message) {
        System.out.println("Push notification gönderildi -> " + deviceToken + ": " + title + " - " + message);
    }
}