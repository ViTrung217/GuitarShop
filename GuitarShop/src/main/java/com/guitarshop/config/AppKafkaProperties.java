package com.guitarshop.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "app.kafka")
public class AppKafkaProperties {

    private String orderTopic = "order-events";

    public String getOrderTopic() {
        return orderTopic;
    }

    public void setOrderTopic(String orderTopic) {
        this.orderTopic = orderTopic;
    }
}
