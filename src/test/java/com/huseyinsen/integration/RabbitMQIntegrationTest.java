package com.huseyinsen.integration;

import org.junit.Test;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.testcontainers.containers.RabbitMQContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
@SpringBootTest
public class RabbitMQIntegrationTest {

    @Container
    public static RabbitMQContainer rabbitMQ = new RabbitMQContainer("rabbitmq:3-management");

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private MyMessageListener myMessageListener;

    @Test
    public void testMessageSendReceive() throws InterruptedException {
        String msg = "test-message";
        rabbitTemplate.convertAndSend("exchange", "routingKey", msg);

        // Listener mesajı alana kadar bekle (örnek için basit Thread.sleep)
        Thread.sleep(1000);

        // myMessageListener üzerinde alınan mesaj kontrol edilir (listener implementasyonuna bağlı)
    }
}