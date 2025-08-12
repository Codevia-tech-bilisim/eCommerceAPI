package com.huseyinsen.event;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class OrderShippedEvent {
    private Long orderId;
    private String trackingNumber;
}