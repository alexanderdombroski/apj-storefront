package edu.byui.apj.storefront.db;

import edu.byui.apj.storefront.db.model.*;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CartTest {

    @Test
    void testCartCreationAndGetters() {
        String cartId = UUID.randomUUID().toString();
        String personId = UUID.randomUUID().toString();
        Cart cart = new Cart();
        cart.setId(cartId);
        cart.setPersonId(personId);
        List<Item> mockItems = new ArrayList<>();
        cart.setItems(mockItems);

        assertEquals(cartId, cart.getId());
        assertEquals(personId, cart.getPersonId());
        assertEquals(mockItems, cart.getItems());
    }

    @Test
    void testCartSetters() {
        Cart cart = new Cart();
        String initialCartId = UUID.randomUUID().toString();
        String initialPersonId = UUID.randomUUID().toString();
        List<Item> initialItems = new ArrayList<>();

        cart.setId(initialCartId);
        assertEquals(initialCartId, cart.getId());

        cart.setPersonId(initialPersonId);
        assertEquals(initialPersonId, cart.getPersonId());

        cart.setItems(initialItems);
        assertEquals(initialItems, cart.getItems());

        String newCartId = UUID.randomUUID().toString();
        String newPersonId = UUID.randomUUID().toString();
        List<Item> newItems = List.of(mock(Item.class), mock(Item.class));

        cart.setId(newCartId);
        assertEquals(newCartId, cart.getId());

        cart.setPersonId(newPersonId);
        assertEquals(newPersonId, cart.getPersonId());

        cart.setItems(newItems);
        assertEquals(newItems, cart.getItems());
    }

    @Test
    void testEqualsAndHashCode() {
        String cartId1 = "cart-123";
        String personId1 = "person-456";
        Cart cart1 = new Cart();
        cart1.setId(cartId1);
        cart1.setPersonId(personId1);
        cart1.setItems(new ArrayList<>());

        Cart cart2 = new Cart();
        cart2.setId(cartId1);
        cart2.setPersonId(personId1);
        cart2.setItems(new ArrayList<>());

        String cartId2 = "cart-789";
        Cart cart3 = new Cart();
        cart3.setId(cartId2);
        cart3.setPersonId(personId1);
        cart3.setItems(new ArrayList<>());

        assertEquals(cart1, cart2);
        assertEquals(cart1.hashCode(), cart2.hashCode());
        assertNotEquals(cart1, cart3);
        assertNotEquals(cart1.hashCode(), cart3.hashCode());

        // Test with null ID
        Cart cart4 = new Cart();
        cart4.setPersonId(personId1);
        cart4.setItems(new ArrayList<>());
        assertNotEquals(cart1, cart4);
        assertNotEquals(cart1.hashCode(), cart4.hashCode());
    }

    @Test
    void testToString() {
        String cartId = "shopping-cart-abc";
        String personId = "user-xyz";
        List<Item> items = List.of(createMockItem("item-1"), createMockItem("item-2"));
        Cart cart = new Cart();
        cart.setId(cartId);
        cart.setPersonId(personId);
        cart.setItems(items);

        String expectedToString = "Cart(id=shopping-cart-abc, personId=user-xyz, items=[" + items.get(0) + ", " + items.get(1) + "])";
        assertEquals(expectedToString, cart.toString());
    }

    private Item createMockItem(String itemId) {
        Item mockItem = mock(Item.class);
        when(mockItem.toString()).thenReturn("Item(id=" + itemId + ", ...)"); // Customize toString for better output
        return mockItem;
    }
}