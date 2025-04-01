package edu.byui.apj.storefront.db;

import edu.byui.apj.storefront.db.controller.CartController;
import edu.byui.apj.storefront.db.model.Cart;
import edu.byui.apj.storefront.db.model.Item;
import edu.byui.apj.storefront.db.service.CartService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;

@WebMvcTest(CartController.class)
public class CartControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CartService cartService;

    @Test
    void getCartNoOrder_shouldReturnOkAndListOfCarts() throws Exception {
        Cart cart1 = new Cart();
        cart1.setId(UUID.randomUUID().toString());
        Cart cart2 = new Cart();
        cart2.setId(UUID.randomUUID().toString());
        List<Cart> carts = Arrays.asList(cart1, cart2);

        when(cartService.getCartsWithoutOrders()).thenReturn(carts);

        mockMvc.perform(MockMvcRequestBuilders.get("/cart/noorder"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(cart1.getId())))
                .andExpect(jsonPath("$[1].id", is(cart2.getId())));

        verify(cartService, times(1)).getCartsWithoutOrders();
    }

    @Test
    void getCartNoOrder_shouldReturnOkAndEmptyList() throws Exception {
        when(cartService.getCartsWithoutOrders()).thenReturn(Collections.emptyList());

        mockMvc.perform(MockMvcRequestBuilders.get("/cart/noorder"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(0)));

        verify(cartService, times(1)).getCartsWithoutOrders();
    }

    @Test
    void getCart_shouldReturnOkAndCart() throws Exception {
        String cartId = UUID.randomUUID().toString();
        Cart cart = new Cart();
        cart.setId(cartId);

        when(cartService.getCart(cartId)).thenReturn(cart);

        mockMvc.perform(MockMvcRequestBuilders.get("/cart/" + cartId))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(cartId)));

        verify(cartService, times(1)).getCart(cartId);
    }

    @Test
    void getCart_shouldReturnNotFound() throws Exception {
        String cartId = UUID.randomUUID().toString();
        when(cartService.getCart(cartId)).thenThrow(new RuntimeException("Cart not found"));

        mockMvc.perform(MockMvcRequestBuilders.get("/cart/" + cartId))
                .andExpect(status().isNotFound()); // Expecting 404 now
        verify(cartService, times(1)).getCart(cartId);
    }

    @Test
    void saveCart_shouldReturnOkAndSavedCart() throws Exception {
        Cart cartToSave = new Cart();
        cartToSave.setPersonId(UUID.randomUUID().toString());
        Cart savedCart = new Cart();
        savedCart.setId(UUID.randomUUID().toString());
        savedCart.setPersonId(cartToSave.getPersonId());

        when(cartService.saveCart(any(Cart.class))).thenReturn(savedCart);

        mockMvc.perform(MockMvcRequestBuilders.post("/cart")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"personId\": \"" + cartToSave.getPersonId() + "\"}")
                )
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(savedCart.getId())))
                .andExpect(jsonPath("$.personId", is(savedCart.getPersonId())));

        verify(cartService, times(1)).saveCart(any(Cart.class));
    }

    @Test
    void addItemToCart_shouldReturnOkAndUpdatedCart() throws Exception {
        String cartId = UUID.randomUUID().toString();
        Item itemToAdd = new Item();
        itemToAdd.setCardId("product-123");
        Cart updatedCart = new Cart();
        updatedCart.setId(cartId);
        updatedCart.setItems(Collections.singletonList(itemToAdd));

        when(cartService.addItemToCart(eq(cartId), any(Item.class))).thenReturn(updatedCart);

        mockMvc.perform(MockMvcRequestBuilders.post("/cart/" + cartId + "/item")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"cardId\": \"" + itemToAdd.getCardId() + "\"}")
                )
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(cartId)))
                .andExpect(jsonPath("$.items", hasSize(1)))
                .andExpect(jsonPath("$.items[0].cardId", is(itemToAdd.getCardId())));

        verify(cartService, times(1)).addItemToCart(eq(cartId), any(Item.class));
    }

    @Test
    void addItemToCart_shouldReturnNotFound_cartNotFound() throws Exception {
        String cartId = UUID.randomUUID().toString();
        Item itemToAdd = new Item();

        when(cartService.addItemToCart(eq(cartId), any(Item.class)))
                .thenThrow(new RuntimeException("Cart not found"));

        mockMvc.perform(MockMvcRequestBuilders.post("/cart/" + cartId + "/item")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"cardId\": \"product-123\"}")
                )
                .andExpect(status().isNotFound()); // Expect 404 now
        verify(cartService, times(1)).addItemToCart(eq(cartId), any(Item.class));
    }

    @Test
    void removeCart_shouldReturnOk() throws Exception {
        String cartId = UUID.randomUUID().toString();

        mockMvc.perform(MockMvcRequestBuilders.delete("/cart/" + cartId))
                .andExpect(status().isOk());

        verify(cartService, times(1)).removeCart(cartId);
    }

    @Test
    void removeItemFromCart_shouldReturnOkAndUpdatedCart() throws Exception {
        String cartId = UUID.randomUUID().toString();
        Long itemIdToRemove = 1L;
        Cart updatedCart = new Cart();
        updatedCart.setId(cartId);
        updatedCart.setItems(Collections.emptyList());

        when(cartService.removeItemFromCart(cartId, itemIdToRemove)).thenReturn(updatedCart);

        mockMvc.perform(MockMvcRequestBuilders.delete("/cart/" + cartId + "/item/" + itemIdToRemove))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(cartId)))
                .andExpect(jsonPath("$.items", hasSize(0)));

        verify(cartService, times(1)).removeItemFromCart(cartId, itemIdToRemove);
    }

    @Test
    void removeItemFromCart_shouldReturnNotFound_cartNotFound() throws Exception {
        String cartId = UUID.randomUUID().toString();
        Long itemIdToRemove = 1L;

        when(cartService.removeItemFromCart(cartId, itemIdToRemove))
                .thenThrow(new RuntimeException("Cart not found"));

        mockMvc.perform(MockMvcRequestBuilders.delete("/cart/" + cartId + "/item/" + itemIdToRemove))
                .andExpect(status().isNotFound()); // Expect 404 now
        verify(cartService, times(1)).removeItemFromCart(cartId, itemIdToRemove);
    }


    @Test
    void updateItemInCart_shouldReturnOkAndUpdatedCart() throws Exception {
        String cartId = UUID.randomUUID().toString();
        Item itemToUpdate = new Item();
        itemToUpdate.setId(1L);
        itemToUpdate.setName("Updated Product");
        Cart updatedCart = new Cart();
        updatedCart.setId(cartId);
        updatedCart.setItems(Collections.singletonList(itemToUpdate));

        when(cartService.updateCartItem(eq(cartId), any(Item.class))).thenReturn(updatedCart);

        mockMvc.perform(MockMvcRequestBuilders.put("/cart/" + cartId + "/item")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\": 1, \"name\": \"Updated Product\"}")
                )
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(cartId)))
                .andExpect(jsonPath("$.items", hasSize(1)))
                .andExpect(jsonPath("$.items[0].id", is(1)))
                .andExpect(jsonPath("$.items[0].name", is("Updated Product")));

        verify(cartService, times(1)).updateCartItem(eq(cartId), any(Item.class));
    }

    @Test
    void updateItemInCart_shouldReturnNotFound_cartNotFound() throws Exception {
        String cartId = UUID.randomUUID().toString();
        Item itemToUpdate = new Item();
        itemToUpdate.setId(1L);

        when(cartService.updateCartItem(eq(cartId), any(Item.class)))
                .thenThrow(new RuntimeException("Cart not found"));

        mockMvc.perform(MockMvcRequestBuilders.put("/cart/" + cartId + "/item")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\": 1, \"name\": \"Updated Product\"}")
                )
                .andExpect(status().isNotFound()); // Expect 404 now
        verify(cartService, times(1)).updateCartItem(eq(cartId), any(Item.class));
    }
}