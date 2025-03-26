package edu.byui.apj.storefront.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
public class Address {
    @EqualsAndHashCode.Include
    Long id;
    String addressLine1;
    String addressLine2;
    String city;
    String state;
    String zipCode;
    String country;
}
