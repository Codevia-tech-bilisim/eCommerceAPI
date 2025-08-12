package com.huseyinsen.event;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PaymentProcessedEvent {
    private Long paymentId;
    private Long orderId;
    private boolean success;
}