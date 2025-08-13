package com.huseyinsen.integration;

import com.huseyinsen.service.Impl.EmailService;
import org.junit.jupiter.api.Test;
import org.modelmapper.internal.bytebuddy.utility.dispatcher.JavaDispatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
@SpringBootTest
public class EmailSendingIntegrationTest {

    @Container
    public static GenericContainer<?> mailHog = new GenericContainer<>("mailhog/mailhog")
            .withExposedPorts(1025, 8025);

    @Autowired
    private EmailService emailService;

    @Test
    public void testSendEmail() {
        // emailService.sendEmail(...) çağrılır
        // MailHog container’ına düşen mailler kontrol edilir (örnek için atlandı)
    }
}