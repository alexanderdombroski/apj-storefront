package edu.byui.apj.storefront.db;

import edu.byui.apj.storefront.db.model.Cart;
import edu.byui.apj.storefront.db.model.Item;
import edu.byui.apj.storefront.db.repository.CartRepository;
import edu.byui.apj.storefront.db.service.CartService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CartServiceTest {

    @Mock
    private CartRepository cartRepository;

    @InjectMocks
    private CartService cartService;

    @Test
    void testAddItemToCart_cartNotFound() {
        String cartId = UUID.randomUUID().toString();
        Item item = new Item();

        when(cartRepository.findById(cartId)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> cartService.addItemToCart(cartId, item));

        verify(cartRepository, times(1)).findById(cartId);
        verify(cartRepository, never()).save(any(Cart.class));
    }

    @Test
    void testAddItemToCart_cartFound() {
        String cartId = UUID.randomUUID().toString();
        Cart cart = new Cart();
        cart.setId(cartId);
        cart.setItems(new ArrayList<>());
        Item item = new Item();
        Long itemId = 1L;
        item.setId(itemId);

        when(cartRepository.findById(cartId)).thenReturn(Optional.of(cart));
        when(cartRepository.save(cart)).thenReturn(cart);

        Cart updatedCart = cartService.addItemToCart(cartId, item);

        assertEquals(1, updatedCart.getItems().size());
        assertEquals(item, updatedCart.getItems().get(0));
        assertEquals(cart, item.getCart());
        verify(cartRepository, times(1)).findById(cartId);
        verify(cartRepository, times(1)).save(cart);
    }

    @Test
    void testRemoveItemFromCart_cartNotFound() {
        String cartId = UUID.randomUUID().toString();
        Long itemId = 1L;

        when(cartRepository.findById(cartId)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> cartService.removeItemFromCart(cartId, itemId));

        verify(cartRepository, times(1)).findById(cartId);
        verify(cartRepository, never()).save(any(Cart.class));
    }

    @Test
    void testRemoveItemFromCart_cartFound_itemNotFound() {
        String cartId = UUID.randomUUID().toString();
        Cart cart = new Cart();
        cart.setId(cartId);
        Item existingItem = new Item();
        existingItem.setId(1L); // Set the ID of the existing item
        cart.setItems(new ArrayList<>(Arrays.asList(existingItem))); // Cart has one item

        when(cartRepository.findById(cartId)).thenReturn(Optional.of(cart));
        when(cartRepository.save(cart)).thenReturn(cart);

        Cart updatedCart = cartService.removeItemFromCart(cartId, 999L); // Item with ID 999 not in cart

        assertEquals(1, updatedCart.getItems().size()); // Cart should still have the original item
        verify(cartRepository, times(1)).findById(cartId);
        verify(cartRepository, times(1)).save(cart);
    }

    @Test
    void testRemoveItemFromCart_cartFound_itemFound() {
        String cartId = UUID.randomUUID().toString();
        Cart cart = new Cart();
        cart.setId(cartId);
        Item itemToRemove = new Item();
        itemToRemove.setId(1L); // Set the ID
        Item item1 = new Item();
        item1.setId(2L);       // Set the ID
        Item item2 = new Item();
        item2.setId(3L);       // Set the ID
        cart.setItems(new ArrayList<>(Arrays.asList(item1, itemToRemove, item2)));

        when(cartRepository.findById(cartId)).thenReturn(Optional.of(cart));
        when(cartRepository.save(cart)).thenReturn(cart);

        Cart updatedCart = cartService.removeItemFromCart(cartId, 1L);

        assertEquals(2, updatedCart.getItems().size());
        assertFalse(updatedCart.getItems().contains(itemToRemove));
        verify(cartRepository, times(1)).findById(cartId);
        verify(cartRepository, times(1)).save(cart);
    }

    @Test
    void testUpdateCartItem_cartNotFound() {
        String cartId = UUID.randomUUID().toString();
        Item item = new Item();

        when(cartRepository.findById(cartId)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> cartService.updateCartItem(cartId, item));

        verify(cartRepository, times(1)).findById(cartId);
        verify(cartRepository, never()).save(any(Cart.class));
    }

    @Test
    void testUpdateCartItem_cartFound_itemNotFoundInCart() {
        String cartId = UUID.randomUUID().toString();
        Cart cart = new Cart();
        cart.setId(cartId);
        Item existingItem = new Item();
        existingItem.setId(1L); // Set the ID of the existing item
        cart.setItems(new ArrayList<>(Arrays.asList(existingItem))); // Cart has one item
        Item updatedItem = new Item();
        updatedItem.setId(999L); // Set the ID of the item to update

        when(cartRepository.findById(cartId)).thenReturn(Optional.of(cart));
        when(cartRepository.save(cart)).thenReturn(cart);

        Cart resultCart = cartService.updateCartItem(cartId, updatedItem);

        assertEquals(2, resultCart.getItems().size()); // Expect 2 items now
        assertEquals(updatedItem, resultCart.getItems().get(1)); // The new item will likely be at the end
        assertEquals(cart, updatedItem.getCart());
        verify(cartRepository, times(1)).findById(cartId);
        verify(cartRepository, times(1)).save(cart);
    }

    @Test
    void testUpdateCartItem_cartFound_itemFoundInCart() {
        String cartId = UUID.randomUUID().toString();
        Cart cart = new Cart();
        cart.setId(cartId);
        Item existingItem = new Item();
        existingItem.setId(1L);
        existingItem.setName("Old Name");
        cart.setItems(new ArrayList<>(Arrays.asList(existingItem)));
        Item updatedItem = new Item();
        updatedItem.setId(1L);
        updatedItem.setName("New Name");

        when(cartRepository.findById(cartId)).thenReturn(Optional.of(cart));
        when(cartRepository.save(cart)).thenReturn(cart);

        Cart resultCart = cartService.updateCartItem(cartId, updatedItem);

        assertEquals(1, resultCart.getItems().size());
        assertEquals("New Name", resultCart.getItems().get(0).getName());
        assertEquals(cart, resultCart.getItems().get(0).getCart());
        verify(cartRepository, times(1)).findById(cartId);
        verify(cartRepository, times(1)).save(cart);
    }

    @Test
    void testGetCart_cartNotFound() {
        String cartId = UUID.randomUUID().toString();

        when(cartRepository.findById(cartId)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> cartService.getCart(cartId));

        verify(cartRepository, times(1)).findById(cartId);
    }

    @Test
    void testGetCart_cartFound() {
        String cartId = UUID.randomUUID().toString();
        Cart cart = new Cart();
        cart.setId(cartId);

        when(cartRepository.findById(cartId)).thenReturn(Optional.of(cart));

        Cart retrievedCart = cartService.getCart(cartId);

        assertEquals(cart, retrievedCart);
        verify(cartRepository, times(1)).findById(cartId);
    }

    @Test
    void testSaveCart() {
        Cart cartToSave = new Cart();
        Cart savedCart = new Cart();
        savedCart.setId(UUID.randomUUID().toString());

        when(cartRepository.save(cartToSave)).thenReturn(savedCart);

        Cart result = cartService.saveCart(cartToSave);

        assertEquals(savedCart.getId(), result.getId());
        verify(cartRepository, times(1)).save(cartToSave);
    }

    @Test
    void testGetCartsWithoutOrders() {
        List<Cart> cartsWithoutOrders = Arrays.asList(new Cart(), new Cart());
        when(cartRepository.findCartsWithoutOrders()).thenReturn(cartsWithoutOrders);

        List<Cart> result = cartService.getCartsWithoutOrders();

        assertEquals(2, result.size());
        assertEquals(cartsWithoutOrders, result);
        verify(cartRepository, times(1)).findCartsWithoutOrders();
    }

    @Test
    void testRemoveCart() {
        String cartId = UUID.randomUUID().toString();

        cartService.removeCart(cartId);

        verify(cartRepository, times(1)).deleteById(cartId);
    }
}