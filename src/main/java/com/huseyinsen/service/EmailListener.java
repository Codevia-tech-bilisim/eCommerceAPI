package com.huseyinsen.service;

import com.huseyinsen.entity.EmailMessage;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.naming.Context;

@Service
@RequiredArgsConstructor
public class EmailListener {

    private final JavaMailSender mailSender;
    private final SpringTemplateEngine templateEngine; // Thymeleaf

    @RabbitListener(queues = "email.queue")
    public void listenEmailQueue(EmailMessage emailMessage) {
        try {
            Context context = new Context();
            context.setVariables(emailMessage.getTemplateModel());

            String body = templateEngine.process(emailMessage.getTemplateName(), context);

            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message);

            helper.setTo(emailMessage.getTo());
            helper.setSubject(emailMessage.getSubject());
            helper.setText(body, true);

            mailSender.send(message);

            // Başarı logu
        } catch (Exception e) {
            // Hata logu ve retry mekanizması
            throw new RuntimeException("Email gönderimi başarısız", e);
        }
    }
}