package com.guitarshop.event;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class OrderEventListener {

    private static final Logger logger = LoggerFactory.getLogger(OrderEventListener.class);

    @KafkaListener(topics = "${app.kafka.order-topic:order-events}")
    public void handleOrderCreated(OrderCreatedEvent event) {
        if (event == null) {
            return;
        }
        logger.info("Order event received: orderId={}, userId={}, total={}, itemCount={}",
                event.getOrderId(), event.getUserId(), event.getTotal(), event.getItemCount());
    }
}
