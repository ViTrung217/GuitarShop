package com.guitarshop.event;

import com.guitarshop.model.Order;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import com.guitarshop.config.AppKafkaProperties;

@Service
public class OrderEventPublisher {

    private static final Logger logger = LoggerFactory.getLogger(OrderEventPublisher.class);

    private final KafkaTemplate<String, OrderCreatedEvent> kafkaTemplate;
    private final AppKafkaProperties appKafkaProperties;

    public OrderEventPublisher(KafkaTemplate<String, OrderCreatedEvent> kafkaTemplate,
                               AppKafkaProperties appKafkaProperties) {
        this.kafkaTemplate = kafkaTemplate;
        this.appKafkaProperties = appKafkaProperties;
    }

    public void publishOrderCreated(Order order) {
        if (order == null || order.getId() == null) {
            return;
        }
        Long userId = order.getUser() != null ? order.getUser().getId() : null;
        int itemCount = order.getItems() != null ? order.getItems().size() : 0;

        OrderCreatedEvent event = new OrderCreatedEvent(
                order.getId(),
                userId,
                order.getTotal(),
                order.getCreatedAt(),
                itemCount
        );

        try {
            kafkaTemplate.send(appKafkaProperties.getOrderTopic(), String.valueOf(order.getId()), event);
        } catch (Exception ex) {
            logger.warn("Failed to publish order event for orderId={}", order.getId(), ex);
        }
    }
}
