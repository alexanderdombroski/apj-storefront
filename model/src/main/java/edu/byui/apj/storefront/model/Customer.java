package edu.byui.apj.storefront.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
public class Customer {
    @EqualsAndHashCode.Include
    Long id;
    String firstName;
    String lastName;
    String email;
    String phone;

}
