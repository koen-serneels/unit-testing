package be.testing.repositories;

import org.springframework.stereotype.Repository;

import be.testing.configuration.spring.profiles.Production;
import be.testing.entities.Customer;

/**
 * @author Koen Serneels
 */
@Repository
@Production
public class CustomerRepositoryImpl implements CustomerRepository {

	// Calls some external subsystem to retrieve the customer information
	// will only work on our real target environments where a simulator is present
	// the goal is that we ommit this implementation in our tests and offer some other
	// way to deal with this, since this implementation cannot be used.

	@Override
	public Customer getCustomer(String username) {
		// Call externel subsystem...
		throw new IllegalStateException("Should not be used in our context");
	}

	@Override
	public void saveCustomer(Customer customer) {
		// Call externel subsystem...
		throw new IllegalStateException("Should not be used in our context");
	}
}
