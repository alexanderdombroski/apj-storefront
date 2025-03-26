package edu.byui.apj.storefront.jms;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class OrderConfirmationController {
    private final JmsTemplate jmsTemplate;

    @Autowired
    public OrderConfirmationController(JmsTemplate jmsTemplate) {
        this.jmsTemplate = jmsTemplate;
    }

    @GetMapping("/confirm/{orderId}")
    public ResponseEntity<String> confirmOrder(@PathVariable String orderId) {
        try {
            jmsTemplate.convertAndSend("orderConfirmationQueue", orderId);
            log.info("JMS message sent for order ID: {}", orderId);
            return ResponseEntity.ok("Order confirm message sent for order ID: " + orderId);
        } catch (Exception e) {
            log.error("Error sending JMS message for order ID: {}", orderId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error processing order confirmation.");
        }
    }

}
