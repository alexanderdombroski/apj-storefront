package edu.byui.apj.storefront.db;

import edu.byui.apj.storefront.db.model.Customer;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class CustomerTest {

    @Test
    void testCustomerCreationAndGetters() {
        Customer customer = new Customer();
        customer.setFirstName("John");
        customer.setLastName("Doe");
        customer.setEmail("john.doe@example.com");
        customer.setPhone("123-456-7890");

        assertNull(customer.getId()); // ID is generated on persistence
        assertEquals("John", customer.getFirstName());
        assertEquals("Doe", customer.getLastName());
        assertEquals("john.doe@example.com", customer.getEmail());
        assertEquals("123-456-7890", customer.getPhone());
    }

    @Test
    void testCustomerSetters() {
        Customer customer = new Customer();

        customer.setFirstName("Jane");
        assertEquals("Jane", customer.getFirstName());

        customer.setLastName("Smith");
        assertEquals("Smith", customer.getLastName());

        customer.setEmail("jane.smith@example.com");
        assertEquals("jane.smith@example.com", customer.getEmail());

        customer.setPhone("987-654-3210");
        assertEquals("987-654-3210", customer.getPhone());
    }

    @Test
    void testEqualsAndHashCode() {
        Customer customer1 = new Customer();
        customer1.setFirstName("Alice");
        customer1.setLastName("Brown");
        customer1.setEmail("alice.brown@example.com");

        Customer customer2 = new Customer();
        customer2.setFirstName("Alice");
        customer2.setLastName("Brown");
        customer2.setEmail("alice.brown@example.com");

        Customer customer3 = new Customer();
        customer3.setFirstName("Bob");
        customer3.setLastName("Green");
        customer3.setEmail("bob.green@example.com");

        assertEquals(customer1, customer2);
        assertEquals(customer1.hashCode(), customer2.hashCode());
        assertNotEquals(customer1, customer3);
        assertNotEquals(customer1.hashCode(), customer3.hashCode());

        // Test with null email (should still be unequal if other fields differ)
        Customer customer4 = new Customer();
        customer4.setFirstName("Alice");
        customer4.setLastName("Brown");

        assertNotEquals(customer1, customer4);
        assertNotEquals(customer1.hashCode(), customer4.hashCode());
    }

    @Test
    void testToString() {
        Customer customer = new Customer();
        customer.setFirstName("Charlie");
        customer.setLastName("White");
        customer.setEmail("charlie.white@example.com");
        customer.setPhone("555-123-4567");

        String expectedToString = "Customer(id=null, firstName=Charlie, lastName=White, email=charlie.white@example.com, phone=555-123-4567)";
        assertEquals(expectedToString, customer.toString());
    }
}