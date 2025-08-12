package com.huseyinsen.service;

import com.huseyinsen.entity.EmailMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailProducer {

    private final RabbitTemplate rabbitTemplate;

    public void sendEmail(EmailMessage emailMessage) {
        rabbitTemplate.convertAndSend("email.exchange", "email.routing.key", emailMessage);
    }
}