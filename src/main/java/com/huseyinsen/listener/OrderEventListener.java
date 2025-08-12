package com.huseyinsen.listener;

import com.huseyinsen.event.OrderCreatedEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class OrderEventListener {

    @EventListener
    public void handleOrderCreated(OrderCreatedEvent event) {
        log.info("OrderCreatedEvent yakalandı: OrderId={}, UserId={}",
                event.getOrderId(), event.getUserId());
        // Burada notification, stok düşme vs. yapılabilir
    }
}