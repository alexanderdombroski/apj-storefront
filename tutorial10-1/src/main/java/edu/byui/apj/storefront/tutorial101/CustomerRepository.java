package edu.byui.apj.storefront.tutorial101;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface CustomerRepository extends CrudRepository<Customer, Long> {

    List<Customer> findByLastName(String lastName);

    List<Customer> findByFirstNameIgnoreCaseOrLastNameIgnoreCase(@Param("firstName") String firstName, @Param("lastName") String lastName);

    Customer findById(long id);
}