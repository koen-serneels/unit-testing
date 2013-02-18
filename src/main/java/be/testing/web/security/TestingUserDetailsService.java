package be.testing.web.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import be.testing.entities.Customer;
import be.testing.repositories.CustomerRepository;

/**
 * @author Koen Serneels
 */
public class TestingUserDetailsService implements UserDetailsService {

	@Autowired
	private CustomerRepository customerRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Customer customer = customerRepository.getCustomer(username);
		return new TestingUserDetails(customer.getUsername(), customer.getPassword());
	}
}
