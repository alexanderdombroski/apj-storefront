package edu.byui.apj.storefront.db;

import edu.byui.apj.storefront.db.model.Cart;
import edu.byui.apj.storefront.db.model.Item;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

public class ItemTest {

    @Test
    void testItemCreationAndGetters() {
        Item item = new Item();
        Long itemId = 123L;
        Cart mockCart = mock(Cart.class);

        item.setId(itemId);
        item.setCart(mockCart);
        item.setCardId("product-abc");
        item.setName("Awesome Gadget");
        item.setPrice(25.99);
        item.setQuantity(2);

        assertEquals(itemId, item.getId());
        assertEquals(mockCart, item.getCart());
        assertEquals("product-abc", item.getCardId());
        assertEquals("Awesome Gadget", item.getName());
        assertEquals(25.99, item.getPrice(), 0.001);
        assertEquals(2, item.getQuantity());
    }

    @Test
    void testItemSetters() {
        Item item = new Item();
        Cart mockCart1 = mock(Cart.class);

        item.setId(456L);
        assertEquals(456L, item.getId());

        item.setCart(mockCart1);
        assertEquals(mockCart1, item.getCart());

        item.setCardId("another-product");
        assertEquals("another-product", item.getCardId());

        item.setName("Cool Thing");
        assertEquals("Cool Thing", item.getName());

        item.setPrice(10.50);
        assertEquals(10.50, item.getPrice(), 0.001);

        item.setQuantity(5);
        assertEquals(5, item.getQuantity());

        Cart mockCart2 = mock(Cart.class);
        item.setCart(mockCart2);
        assertEquals(mockCart2, item.getCart());
    }

    @Test
    void testEqualsAndHashCode() {
        Item item1 = new Item();
        item1.setId(789L);
        item1.setCardId("unique-item");
        item1.setName("Unique Item");

        Item item2 = new Item();
        item2.setId(789L);
        item2.setCardId("unique-item");
        item2.setName("Unique Item");

        Item item3 = new Item();
        item3.setId(101L);
        item3.setCardId("unique-item");
        item3.setName("Unique Item");

        Item item4 = new Item();
        item4.setId(789L);
        item4.setCardId("different-item");
        item4.setName("Unique Item");

        assertEquals(item1, item2);
        assertEquals(item1.hashCode(), item2.hashCode());

        assertNotEquals(item1, item3);
        assertNotEquals(item1.hashCode(), item3.hashCode());

        assertNotEquals(item1, item4);
        assertNotEquals(item1.hashCode(), item4.hashCode());

        // Test with null ID (should be unequal if other identifying fields are the same)
        Item item5 = new Item();
        item5.setCardId("unique-item");
        item5.setName("Unique Item");
        assertNotEquals(item1, item5);
        assertNotEquals(item1.hashCode(), item5.hashCode());
    }

    @Test
    void testToString() {
        Item item = new Item();
        item.setId(222L);
        Cart mockCart = mock(Cart.class);
        item.setCart(mockCart);
        item.setCardId("special-offer");
        item.setName("Special Offer Product");
        item.setPrice(99.99);
        item.setQuantity(1);

        String expectedToString = "Item(id=222, cart=" + mockCart + ", cardId=special-offer, name=Special Offer Product, price=99.99, quantity=1)";
        assertEquals(expectedToString, item.toString());
    }
}