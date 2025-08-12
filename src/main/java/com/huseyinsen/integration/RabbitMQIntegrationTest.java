package com.huseyinsen.integration;

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