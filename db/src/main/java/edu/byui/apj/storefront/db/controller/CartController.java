package edu.byui.apj.storefront.db.controller;

import edu.byui.apj.storefront.db.model.Cart;
import edu.byui.apj.storefront.db.model.Item;
import edu.byui.apj.storefront.db.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException; // Import this

import java.util.List;

@RestController
@RequestMapping("/cart")
@CrossOrigin(origins = {"http://localhost:8080","http://localhost:8083"})
public class CartController {

    private final CartService cartService;

    @Autowired
    CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @GetMapping("/noorder")
    public ResponseEntity<List<Cart>> getCartNoOrder() {
        List<Cart> mycarts = cartService.getCartsWithoutOrders();
        return ResponseEntity.ok(mycarts);
    }
    @GetMapping("/{cartId}")
    public ResponseEntity<Cart> getCart(@PathVariable String cartId) {
        try {
            Cart mycart = cartService.getCart(cartId);
            return ResponseEntity.ok(mycart);
        } catch (RuntimeException e) {
            if (e.getMessage().equals("Cart not found")) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Cart not found");
            }
            throw e; // Re-throw other RuntimeExceptions if needed
        }
    }
    @PostMapping
    public ResponseEntity<Cart> saveCart(@RequestBody Cart cart) {
        Cart savedCart = cartService.saveCart(cart);
        return ResponseEntity.ok(savedCart);
    }
    @PostMapping("/{cartId}/item")
    public ResponseEntity<Cart> addItemToCart(@PathVariable String cartId,
                                              @RequestBody Item item) {
        try {
            Cart updatedCart = cartService.addItemToCart(cartId, item);
            return ResponseEntity.ok(updatedCart);
        } catch (RuntimeException e) {
            if (e.getMessage().equals("Cart not found")) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Cart not found");
            }
            throw e;
        }
    }
    @DeleteMapping("/{cartId}")
    public void removeCart(@PathVariable String cartId) {
        cartService.removeCart(cartId);
    }
    @DeleteMapping("/{cartId}/item/{itemId}")
    public ResponseEntity<Cart> removeItemFromCart(@PathVariable String cartId,
                                                   @PathVariable Long itemId) {
        try {
            Cart updatedCart = cartService.removeItemFromCart(cartId, itemId);
            return ResponseEntity.ok(updatedCart);
        } catch (RuntimeException e) {
            if (e.getMessage().equals("Cart not found")) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Cart not found");
            }
            throw e;
        }
    }
    @PutMapping("/{cartId}/item")
    public ResponseEntity<Cart> updateItemInCart(@PathVariable String cartId,
                                                 @RequestBody Item item) {
        try {
            Cart updatedCart = cartService.updateCartItem(cartId, item);
            return ResponseEntity.ok(updatedCart);
        } catch (RuntimeException e) {
            if (e.getMessage().equals("Cart not found")) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Cart not found");
            }
            throw e;
        }
    }
}