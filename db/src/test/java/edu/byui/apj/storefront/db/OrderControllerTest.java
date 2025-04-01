package edu.byui.apj.storefront.db;


import edu.byui.apj.storefront.db.controller.OrderController;
import edu.byui.apj.storefront.db.model.CardOrder;
import edu.byui.apj.storefront.db.model.Cart;
import edu.byui.apj.storefront.db.model.Customer;
import edu.byui.apj.storefront.db.model.Address;
import edu.byui.apj.storefront.db.service.OrderService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.Matchers.is;

@WebMvcTest(OrderController.class)
public class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrderService orderService;

    @Test
    void saveOrder_shouldReturnOkAndSavedOrder() throws Exception {
        CardOrder orderToSave = new CardOrder();
        Cart cart = new Cart();
        cart.setId("test-cart-id");
        orderToSave.setCart(cart);
        Customer customer = new Customer();
        customer.setId(1L);
        orderToSave.setCustomer(customer);
        Address address = new Address();
        address.setId(1L);
        orderToSave.setShippingAddress(address);

        CardOrder savedOrder = new CardOrder();
        savedOrder.setId(123L);
        savedOrder.setCart(cart);
        savedOrder.setCustomer(customer);
        savedOrder.setShippingAddress(address);

        when(orderService.saveOrder(any(CardOrder.class))).thenReturn(savedOrder);

        mockMvc.perform(MockMvcRequestBuilders.post("/order")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"cart\": {\"id\": \"test-cart-id\"}, \"customer\": {\"id\": 1}, \"shippingAddress\": {\"id\": 1}}")
                )
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(123)))
                .andExpect(jsonPath("$.cart.id", is("test-cart-id")))
                .andExpect(jsonPath("$.customer.id", is(1)))
                .andExpect(jsonPath("$.shippingAddress.id", is(1)));

        verify(orderService, times(1)).saveOrder(any(CardOrder.class));
    }

    @Test
    void getOrder_shouldReturnOkAndOrder() throws Exception {
        Long orderId = 456L;
        CardOrder order = new CardOrder();
        order.setId(orderId);
        Cart cart = new Cart();
        cart.setId("test-cart-id");
        order.setCart(cart);

        when(orderService.getOrder(orderId)).thenReturn(Optional.of(order));

        mockMvc.perform(MockMvcRequestBuilders.get("/order/" + orderId))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(456)))
                .andExpect(jsonPath("$.cart.id", is("test-cart-id")));

        verify(orderService, times(1)).getOrder(orderId);
    }

    @Test
    void getOrder_shouldReturnNotFound() throws Exception {
        Long orderId = 789L;

        when(orderService.getOrder(orderId)).thenReturn(Optional.empty());

        mockMvc.perform(MockMvcRequestBuilders.get("/order/" + orderId))
                .andExpect(status().isNotFound());

        verify(orderService, times(1)).getOrder(orderId);
    }
}
