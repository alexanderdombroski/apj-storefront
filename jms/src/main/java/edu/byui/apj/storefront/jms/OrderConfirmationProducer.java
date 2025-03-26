package edu.byui.apj.storefront.jms;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class OrderConfirmationProducer {

    private final JmsTemplate jmsTemplate;

    @Autowired
    public OrderConfirmationProducer(JmsTemplate jmsTemplate) {
        this.jmsTemplate = jmsTemplate;
    }

    public void sendOrderConfirmation(String orderId) {
        jmsTemplate.convertAndSend("orderQueue", orderId);
        log.info("Order confirmation message sent for order ID: {}", orderId);
    }
}
