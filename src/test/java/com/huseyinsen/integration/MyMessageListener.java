package com.huseyinsen.integration;

import com.huseyinsen.config.RabbitMQConfig;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

@Component
public class MyMessageListener {

    // Alınan mesajları testlerde kontrol edebilmek için bir kuyruk kullanıyoruz
    private BlockingQueue<String> messages = new LinkedBlockingQueue<>();

    @RabbitListener(queues = RabbitMQConfig.QUEUE_NAME)
    public void receiveMessage(String message) {
        System.out.println("Received message: " + message);
        messages.add(message);
    }

    // Testlerde kullanılan yardımcı metod
    public String getNextMessage() throws InterruptedException {
        // Kuyruktan mesaj al, yoksa bekle
        return messages.take();
    }

    // Testler için kuyruğu temizleme metodu
    public void clearMessages() {
        messages.clear();
    }
}