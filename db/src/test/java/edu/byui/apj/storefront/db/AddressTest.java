package edu.byui.apj.storefront.db;

import edu.byui.apj.storefront.db.model.Address;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class AddressTest {

    @Test
    void testAddressCreationAndGetters() {
        Address address = new Address();
        address.setAddressLine1("123 Main St");
        address.setAddressLine2("Apt 4B");
        address.setCity("Rexburg");
        address.setState("ID");
        address.setZipCode("83440");
        address.setCountry("USA");

        assertEquals("123 Main St", address.getAddressLine1());
        assertEquals("Apt 4B", address.getAddressLine2());
        assertEquals("Rexburg", address.getCity());
        assertEquals("ID", address.getState());
        assertEquals("83440", address.getZipCode());
        assertEquals("USA", address.getCountry());
        assertNull(address.getId()); // ID is generated on persistence
    }

    @Test
    void testAddressSetters() {
        Address address = new Address();

        address.setAddressLine1("456 Oak Ave");
        assertEquals("456 Oak Ave", address.getAddressLine1());

        address.setAddressLine2("Suite 100");
        assertEquals("Suite 100", address.getAddressLine2());

        address.setCity("Idaho Falls");
        assertEquals("Idaho Falls", address.getCity());

        address.setState("ID");
        assertEquals("ID", address.getState());

        address.setZipCode("83402");
        assertEquals("83402", address.getZipCode());

        address.setCountry("United States");
        assertEquals("United States", address.getCountry());
    }

    @Test
    void testEqualsAndHashCode() {
        Address address1 = new Address();
        address1.setAddressLine1("123 Main St");
        address1.setCity("Rexburg");
        address1.setZipCode("83440");

        Address address2 = new Address();
        address2.setAddressLine1("123 Main St");
        address2.setCity("Rexburg");
        address2.setZipCode("83440");

        Address address3 = new Address();
        address3.setAddressLine1("456 Oak Ave");
        address3.setCity("Rexburg");
        address3.setZipCode("83440");

        assertEquals(address1, address2);
        assertEquals(address1.hashCode(), address2.hashCode());
        assertNotEquals(address1, address3);
        assertNotEquals(address1.hashCode(), address3.hashCode());
    }

    @Test
    void testToString() {
        Address address = new Address();
        address.setAddressLine1("789 Pine Ln");
        address.setAddressLine2("");
        address.setCity("Rigby");
        address.setState("ID");
        address.setZipCode("83442");
        address.setCountry("US");

        String expectedToString = "Address(id=null, addressLine1=789 Pine Ln, addressLine2=, city=Rigby, state=ID, zipCode=83442, country=US)";
        assertEquals(expectedToString, address.toString());
    }
}