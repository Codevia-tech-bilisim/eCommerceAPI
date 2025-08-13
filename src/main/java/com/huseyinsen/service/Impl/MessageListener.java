package com.huseyinsen.service.Impl;

import com.huseyinsen.config.RabbitMQConfig;
import com.huseyinsen.dto.MyMessage;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
public class MessageListener {

    @RabbitListener(queues = RabbitMQConfig.QUEUE_NAME)
    public void receiveMessage(String message) {
        System.out.println("Received message: " + message);
    }

    @RabbitListener(queues = RabbitMQConfig.QUEUE_NAME, containerFactory = "rabbitListenerContainerFactory")
    public void receiveMessage(MyMessage message) { //mymessage eksik
    }
}