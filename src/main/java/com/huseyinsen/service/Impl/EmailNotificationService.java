package com.huseyinsen.service.Impl;

import com.huseyinsen.service.INotificationService;
import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.thymeleaf.context.Context;
import org.thymeleaf.TemplateEngine;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EmailNotificationService implements INotificationService {

    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;

    @Override
    public void sendEmail(String to, String subject, String templateName, Object model) {
        try {
            // Thymeleaf context oluştur
            Context context = new Context();
            context.setVariables((java.util.Map<String, Object>) model);

            // Template’i HTML’e çevir
            String htmlContent = templateEngine.process(templateName, context);

            // MimeMessage oluştur
            MimeMessage mimeMessage = mailSender.createMimeMessage();

            // Helper ile alıcı, konu ve içerik set et
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(htmlContent, true);

            // Mail gönder
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