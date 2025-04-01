package edu.byui.apj.storefront.db;

import edu.byui.apj.storefront.db.model.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

import java.util.Date;

public class CardOrderTest {

    @Test
    void testCardOrderCreationAndGetters() {
        CardOrder order = new CardOrder();
        Cart mockCart = mock(Cart.class);
        Address mockAddress = mock(Address.class);
        Customer mockCustomer = mock(Customer.class);
        Date orderDate = new Date();

        order.setCart(mockCart);
        order.setShippingAddress(mockAddress);
        order.setCustomer(mockCustomer);
        order.setOrderDate(orderDate);
        order.setConfirmationSent(true);
        order.setShipMethod("UPS Ground");
        order.setOrderNotes("Leave at front door");
        order.setSubtotal(100.00);
        order.setTotal(107.00);
        order.setTax(7.00);

        assertNull(order.getId()); // ID is generated on persistence
        assertEquals(mockCart, order.getCart());
        assertEquals(mockAddress, order.getShippingAddress());
        assertEquals(mockCustomer, order.getCustomer());
        assertEquals(orderDate, order.getOrderDate());
        assertTrue(order.isConfirmationSent());
        assertEquals("UPS Ground", order.getShipMethod());
        assertEquals("Leave at front door", order.getOrderNotes());
        assertEquals(100.00, order.getSubtotal(), 0.001);
        assertEquals(107.00, order.getTotal(), 0.001);
        assertEquals(7.00, order.getTax(), 0.001);
    }

    @Test
    void testCardOrderSetters() {
        CardOrder order = new CardOrder();
        Cart mockCart1 = mock(Cart.class);
        Address mockAddress1 = mock(Address.class);
        Customer mockCustomer1 = mock(Customer.class);
        Date orderDate1 = new Date();

        order.setCart(mockCart1);
        assertEquals(mockCart1, order.getCart());

        order.setShippingAddress(mockAddress1);
        assertEquals(mockAddress1, order.getShippingAddress());

        order.setCustomer(mockCustomer1);
        assertEquals(mockCustomer1, order.getCustomer());

        order.setOrderDate(orderDate1);
        assertEquals(orderDate1, order.getOrderDate());

        order.setConfirmationSent(false);
        assertFalse(order.isConfirmationSent());

        order.setShipMethod("FedEx Express");
        assertEquals("FedEx Express", order.getShipMethod());

        order.setOrderNotes("Handle with care");
        assertEquals("Handle with care", order.getOrderNotes());

        order.setSubtotal(50.00);
        assertEquals(50.00, order.getSubtotal(), 0.001);

        order.setTotal(53.50);
        assertEquals(53.50, order.getTotal(), 0.001);

        order.setTax(3.50);
        assertEquals(3.50, order.getTax(), 0.001);
    }

    @Test
    void testEqualsAndHashCode() {
        CardOrder order1 = new CardOrder();
        Cart mockCart = mock(Cart.class);
        Address mockAddress = mock(Address.class);
        Customer mockCustomer = mock(Customer.class);
        Date orderDate = new Date();

        order1.setCart(mockCart);
        order1.setShippingAddress(mockAddress);
        order1.setCustomer(mockCustomer);
        order1.setOrderDate(orderDate);
        order1.setTotal(107.00);

        CardOrder order2 = new CardOrder();
        order2.setCart(mockCart);
        order2.setShippingAddress(mockAddress);
        order2.setCustomer(mockCustomer);
        order2.setOrderDate(orderDate);
        order2.setTotal(107.00);

        CardOrder order3 = new CardOrder();
        Cart mockCartDifferent = mock(Cart.class);
        order3.setCart(mockCartDifferent);
        order3.setShippingAddress(mockAddress);
        order3.setCustomer(mockCustomer);
        order3.setOrderDate(orderDate);
        order3.setTotal(107.00);

        assertEquals(order1, order2);
        assertEquals(order1.hashCode(), order2.hashCode());
        assertNotEquals(order1, order3);
        assertNotEquals(order1.hashCode(), order3.hashCode());

        // Test with null cart
        order1.setCart(null);
        assertNotEquals(order1, order2);
        assertNotEquals(order1.hashCode(), order2.hashCode());
        order2.setCart(null);
        assertEquals(order1, order2);
        assertEquals(order1.hashCode(), order2.hashCode());
    }

    @Test
    void testToString() {
        CardOrder order = new CardOrder();
        Cart mockCart = mock(Cart.class);
        Address mockAddress = mock(Address.class);
        Customer mockCustomer = mock(Customer.class);
        Date orderDate = new Date();

        order.setCart(mockCart);
        order.setShippingAddress(mockAddress);
        order.setCustomer(mockCustomer);
        order.setOrderDate(orderDate);
        order.setConfirmationSent(false);
        order.setShipMethod("Standard");
        order.setOrderNotes("");
        order.setSubtotal(75.50);
        order.setTotal(80.80);
        order.setTax(5.30);

        String expectedToString = "CardOrder(id=null, cart=" + mockCart + ", shippingAddress=" + mockAddress + ", customer=" + mockCustomer + ", orderDate=" + orderDate + ", confirmationSent=false, shipMethod=Standard, orderNotes=, subtotal=75.5, total=80.8, tax=5.3)";
        assertEquals(expectedToString, order.toString());
    }
}