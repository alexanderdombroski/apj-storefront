package edu.byui.apj.storefront.db;

import edu.byui.apj.storefront.db.model.Cart;
import edu.byui.apj.storefront.db.repository.CartRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CartRepositoryTest {

    @Mock
    private CartRepository cartRepository; // Mock the repository

    @Test
    void testFindCartsWithoutOrders_noOrdersExist() {
        Cart cart1 = new Cart();
        cart1.setId(UUID.randomUUID().toString());
        Cart cart2 = new Cart();
        cart2.setId(UUID.randomUUID().toString());

        when(cartRepository.findCartsWithoutOrders()).thenReturn(Arrays.asList(cart1, cart2));

        List<Cart> cartsWithoutOrders = cartRepository.findCartsWithoutOrders();
        assertEquals(2, cartsWithoutOrders.size());
        assertTrue(cartsWithoutOrders.contains(cart1));
        assertTrue(cartsWithoutOrders.contains(cart2));
    }

    @Test
    void testFindCartsWithoutOrders_someCartsHaveOrders() {
        Cart cartWithoutOrder1 = new Cart();
        cartWithoutOrder1.setId(UUID.randomUUID().toString());
        Cart cartWithoutOrder2 = new Cart();
        cartWithoutOrder2.setId(UUID.randomUUID().toString());

        // We would need to mock the underlying JpaRepository methods if findCartsWithoutOrders used them internally
        // In this case, we directly mock the custom query method
        when(cartRepository.findCartsWithoutOrders()).thenReturn(Arrays.asList(cartWithoutOrder1, cartWithoutOrder2));

        List<Cart> cartsWithoutOrders = cartRepository.findCartsWithoutOrders();
        assertEquals(2, cartsWithoutOrders.size());
        assertTrue(cartsWithoutOrders.contains(cartWithoutOrder1));
        assertTrue(cartsWithoutOrders.contains(cartWithoutOrder2));
    }

    @Test
    void testFindCartsWithoutOrders_allCartsHaveOrders() {
        when(cartRepository.findCartsWithoutOrders()).thenReturn(Collections.emptyList());

        List<Cart> cartsWithoutOrders = cartRepository.findCartsWithoutOrders();
        assertTrue(cartsWithoutOrders.isEmpty());
    }

    @Test
    void testFindCartsWithoutOrders_noCartsExist() {
        when(cartRepository.findCartsWithoutOrders()).thenReturn(Collections.emptyList());

        List<Cart> cartsWithoutOrders = cartRepository.findCartsWithoutOrders();
        assertTrue(cartsWithoutOrders.isEmpty());
    }

    // You would also need to mock the inherited JpaRepository methods if you were testing code that called them directly
    @Test
    void testFindById() {
        String cartId = UUID.randomUUID().toString();
        Cart cart = new Cart();
        cart.setId(cartId);

        when(cartRepository.findById(cartId)).thenReturn(Optional.of(cart));

        Optional<Cart> foundCart = cartRepository.findById(cartId);
        assertTrue(foundCart.isPresent());
        assertEquals(cartId, foundCart.get().getId());
    }

}