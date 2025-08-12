package com.huseyinsen.integration;

import org.springframework.beans.factory.annotation.Autowired;

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