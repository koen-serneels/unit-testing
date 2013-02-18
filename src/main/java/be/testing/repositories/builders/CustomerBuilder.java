package be.testing.repositories.builders;

import org.apache.commons.codec.digest.DigestUtils;

import be.testing.entities.Customer;

/**
 * @author Koen Serneels
 */
public class CustomerBuilder {

	private Customer instance = new Customer();

	public CustomerBuilder firstName(String firstname) {
		instance.setFirstname(firstname);
		return this;
	}

	public CustomerBuilder lastName(String lastname) {
		instance.setLastname(lastname);
		return this;
	}

	public CustomerBuilder credentials(String username, String password) {
		instance.setUsername(username);
		instance.setPassword(DigestUtils.sha256Hex(password + "{" + username + "}"));
		return this;
	}

	public Customer build() {
		return instance;
	}
}
