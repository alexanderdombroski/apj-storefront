package edu.byui.apj.storefront.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

@Data
public class CardOrder {
    @EqualsAndHashCode.Include
    Long id;
    Cart cart;
    Address shippingAddress;
    Customer customer;
    Date orderDate;
    boolean confirmationSent;
    String shipMethod;
    String orderNotes;
    Double subtotal;
    Double total;
    Double tax;

}
