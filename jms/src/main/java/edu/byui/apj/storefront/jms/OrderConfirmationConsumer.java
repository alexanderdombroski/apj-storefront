package edu.byui.apj.storefront.jms;

import edu.byui.apj.storefront.model.CardOrder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
@Slf4j
public class OrderConfirmationConsumer {

    private final WebClient webClient;

    @Autowired
    public OrderConfirmationConsumer(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("http://localhost:8083").build();
    }

    @JmsListener(destination = "orderQueue")
    public void receiveOrderConfirmation(String orderId) {
        log.info("Received order confirmation for order ID: {}", orderId);

        webClient.get()
                .uri("/order/{orderId}", orderId)
                .retrieve()
                .bodyToMono(CardOrder.class)
                .subscribe(
                        cardOrder -> {
                            log.info("Order details: {}", cardOrder);
                        },
                        error -> {
                            log.error("Error retrieving order details for order ID: {}", orderId, error);
                        }
                );
    }
}