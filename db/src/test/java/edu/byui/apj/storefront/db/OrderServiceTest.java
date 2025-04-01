package edu.byui.apj.storefront.db;

import edu.byui.apj.storefront.db.model.CardOrder;
import edu.byui.apj.storefront.db.model.Cart;
import edu.byui.apj.storefront.db.repository.OrderRepository;
import edu.byui.apj.storefront.db.service.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private CartService cartService;

    @InjectMocks
    private OrderService orderService;

    @Test
    void testSaveOrder_cartNotFound() {
        CardOrder orderToSave = new CardOrder();
        Cart cart = new Cart();
        String cartId = UUID.randomUUID().toString();
        cart.setId(cartId);
        orderToSave.setCart(cart);

        when(cartService.getCart(cartId)).thenThrow(new RuntimeException("Cart not found"));

        assertThrows(RuntimeException.class, () -> orderService.saveOrder(orderToSave));

        verify(cartService, times(1)).getCart(cartId);
        verify(orderRepository, never()).save(any(CardOrder.class));
    }

    @Test
    void testSaveOrder_cartFound() {
        CardOrder orderToSave = new CardOrder();
        Cart cart = new Cart();
        String cartId = UUID.randomUUID().toString();
        cart.setId(cartId);
        orderToSave.setCart(cart);

        Cart retrievedCart = new Cart();
        retrievedCart.setId(cartId);

        CardOrder savedOrder = new CardOrder();
        Long orderId = 123L;
        savedOrder.setId(orderId);
        savedOrder.setCart(retrievedCart);

        when(cartService.getCart(cartId)).thenReturn(retrievedCart);
        when(orderRepository.save(orderToSave)).thenReturn(savedOrder);

        CardOrder result = orderService.saveOrder(orderToSave);

        assertNotNull(result.getId());
        assertEquals(orderId, result.getId());
        assertEquals(retrievedCart, result.getCart());
        verify(cartService, times(1)).getCart(cartId);
        verify(orderRepository, times(1)).save(orderToSave);
    }

    @Test
    void testGetOrder_orderFound() {
        Long orderId = 456L;
        CardOrder order = new CardOrder();
        order.setId(orderId);

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));

        Optional<CardOrder> result = orderService.getOrder(orderId);

        assertTrue(result.isPresent());
        assertEquals(orderId, result.get().getId());
        verify(orderRepository, times(1)).findById(orderId);
    }

    @Test
    void testGetOrder_orderNotFound() {
        Long orderId = 789L;

        when(orderRepository.findById(orderId)).thenReturn(Optional.empty());

        Optional<CardOrder> result = orderService.getOrder(orderId);

        assertTrue(result.isEmpty());
        verify(orderRepository, times(1)).findById(orderId);
    }
}