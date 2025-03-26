package edu.byui.apj.storefront.jms;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import edu.byui.apj.storefront.model.Cart;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Component
@Slf4j
public class CartCleanupJob {

    private final WebClient webClient;

    @Autowired
    public CartCleanupJob(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("http://localhost:8083").build();
    }

    public List<Cart> getCartsWithoutOrders() {
        try {
            return webClient.get()
                    .uri("/cart/noorder")
                    .retrieve()
                    .bodyToFlux(Cart.class)
                    .collectList()
                    .block();
        } catch (Exception e) {
            log.error("Error retrieving carts without orders", e);
            throw new RuntimeException("Error retrieving carts without orders", e);
        }
    }

    public void cleanupCart(String cartId) {
        log.info("Attempting to delete cart with ID: {}", cartId);
        webClient.delete()
                .uri("/cart/{cartId}", cartId)
                .retrieve()
                .toBodilessEntity()
                .subscribe(
                        response -> {
                            if (response.getStatusCode().is2xxSuccessful()) {
                                log.info("Successfully deleted cart with ID: {}", cartId);
                            } else {
                                log.warn("Failed to delete cart with ID: {}. Received status code: {}", cartId, response.getStatusCode());
                            }
                        },
                        error -> {
                            log.error("Error deleting cart with ID: {}: {}", cartId, error.getMessage());
                        }
                );
    }

    @Scheduled(cron = "0 0 2 * * *")
    public void cleanupCarts() {
        log.info("Starting cart cleanup job");
        List<Cart> cartsToCleanup = getCartsWithoutOrders();

        if (cartsToCleanup != null && !cartsToCleanup.isEmpty()) {
            ExecutorService executorService = Executors.newFixedThreadPool(2);

            cartsToCleanup.forEach(cart -> executorService.submit(() -> cleanupCart(cart.getId())));

            executorService.shutdown();
            try {
                if (!executorService.awaitTermination(1, TimeUnit.MINUTES)) {
                    log.warn("Cart cleanup job did not finish within 1 minute.");
                }
            } catch (InterruptedException e) {
                log.warn("Cart cleanup job interrupted.", e);
            }

            log.info("Cart cleanup complete");
        } else {
            log.info("No carts to cleanup");
        }
    }

}