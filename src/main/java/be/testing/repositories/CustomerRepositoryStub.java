package be.testing.repositories;

import java.util.HashMap;
import java.util.Map;

import org.springframework.context.annotation.Profile;
import org.springframework.jmx.export.annotation.ManagedOperation;
import org.springframework.jmx.export.annotation.ManagedResource;
import org.springframework.stereotype.Repository;

import be.testing.configuration.spring.profiles.Tomcat;
import be.testing.configuration.spring.profiles.TomcatSelenium;
import be.testing.entities.Customer;

/**
 * @author Koen Serneels
 */
@Repository
@ManagedResource(objectName = "testing:name=CustomerRepositoryStub")
@Profile({ TomcatSelenium.name, Tomcat.name })
public class CustomerRepositoryStub implements CustomerRepositoryMBean {

	private Map<String, Customer> customerStore = new HashMap<String, Customer>();

	@ManagedOperation
	@Override
	public void saveCustomer(Customer customer) {
		customerStore.put(customer.getUsername(), customer);
	}

	@Override
	public Customer getCustomer(String username) {
		return customerStore.get(username);
	}

	@ManagedOperation
	@Override
	public void clearAll() {
		customerStore.clear();
	}
}