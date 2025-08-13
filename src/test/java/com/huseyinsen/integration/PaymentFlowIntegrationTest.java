package com.huseyinsen.integration;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
public class PaymentFlowIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testProcessPayment() throws Exception {
        mockMvc.perform(post("/api/payments/process")
                        .param("orderId", "1")
                        .param("amount", "100.00")
                        .param("method", "CREDIT_CARD"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("SUCCESS"));
    }
}