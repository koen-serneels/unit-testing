package be.testing.repositories;

import be.testing.entities.Customer;

/**
 * @author Koen Serneels
 */
public interface CustomerRepository {

	void saveCustomer(Customer customer);

	Customer getCustomer(String username);
}
