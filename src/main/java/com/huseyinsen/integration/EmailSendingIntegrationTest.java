package com.huseyinsen.integration;

import org.modelmapper.internal.bytebuddy.utility.dispatcher.JavaDispatcher;
import org.springframework.beans.factory.annotation.Autowired;

@Testcontainers
@SpringBootTest
public class EmailSendingIntegrationTest {

    @JavaDispatcher.Container
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