package edu.byui.apj.storefront.db;
import edu.byui.apj.storefront.db.model.CardOrder;
import edu.byui.apj.storefront.db.repository.OrderRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
public class OrderRepositoryTest {

    @Mock
    private OrderRepository orderRepository; // Mock the repository

    private CardOrder createTestOrder(Long id) {
        CardOrder order = new CardOrder();
        order.setId(id);
        // You might set other properties if they are relevant to the tests
        return order;
    }

    @Test
    void testSaveOrder() {
        CardOrder orderToSave = createTestOrder(null); // ID will be generated
        CardOrder savedOrder = createTestOrder(1L);

        when(orderRepository.save(orderToSave)).thenReturn(savedOrder);

        CardOrder result = orderRepository.save(orderToSave);
        assertNotNull(result.getId());
        assertEquals(1L, result.getId());
        verify(orderRepository, times(1)).save(orderToSave); // Verify the save method was called
    }

    @Test
    void testFindOrderById() {
        Long orderId = 1L;
        CardOrder order = createTestOrder(orderId);

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));

        Optional<CardOrder> foundOrder = orderRepository.findById(orderId);
        assertTrue(foundOrder.isPresent());
        assertEquals(orderId, foundOrder.get().getId());
        verify(orderRepository, times(1)).findById(orderId);
    }

    @Test
    void testFindNonExistingOrderById() {
        Long nonExistingId = 999L;

        when(orderRepository.findById(nonExistingId)).thenReturn(Optional.empty());

        Optional<CardOrder> foundOrder = orderRepository.findById(nonExistingId);
        assertTrue(foundOrder.isEmpty());
        verify(orderRepository, times(1)).findById(nonExistingId);
    }

    @Test
    void testUpdateOrder() {
        Long orderId = 1L;
        CardOrder orderToUpdate = createTestOrder(orderId);

        when(orderRepository.save(orderToUpdate)).thenReturn(orderToUpdate);

        CardOrder updatedOrder = orderRepository.save(orderToUpdate);
        assertEquals(orderId, updatedOrder.getId());
        verify(orderRepository, times(1)).save(orderToUpdate);
    }

    @Test
    void testDeleteOrder() {
        Long orderId = 1L;

        orderRepository.deleteById(orderId);
        verify(orderRepository, times(1)).deleteById(orderId);
    }

    @Test
    void testFindAllOrders() {
        CardOrder order1 = createTestOrder(1L);
        CardOrder order2 = createTestOrder(2L);
        List<CardOrder> allOrders = Arrays.asList(order1, order2);

        when(orderRepository.findAll()).thenReturn(allOrders);

        Iterable<CardOrder> result = orderRepository.findAll();
        int count = 0;
        for (CardOrder order : result) {
            count++;
        }
        assertEquals(2, count);
        verify(orderRepository, times(1)).findAll();
    }
}