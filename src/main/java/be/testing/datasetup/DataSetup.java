package be.testing.datasetup;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import be.testing.configuration.spring.profiles.Tomcat;
import be.testing.entities.Customer;
import be.testing.repositories.CustomerRepository;
import be.testing.repositories.builders.CustomerBuilder;
import be.testing.repositories.builders.OrderBuilder;
import be.testing.repositories.builders.ProductBuilder;
import be.testing.support.TestDataBuilderManager;

/**
 * @author Koen Serneels
 */
@Component
@Tomcat
public class DataSetup implements InitializingBean {

	@Autowired
	private PlatformTransactionManager platformTransactionManager;
	@Autowired
	private CustomerRepository customerRepository;
	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public void afterPropertiesSet() throws Exception {
		new TransactionTemplate(platformTransactionManager).execute(new TransactionCallback<Void>() {
			@Override
			public Void doInTransaction(TransactionStatus status) {
				TestDataBuilderManager.init(entityManager);

				// Order
				new OrderBuilder() {
					{
						name("testorder");
						date(new Date());
						addProduct(new ProductBuilder() {
							{
								name("SGS3");
								description("Smartphone");
								price(new BigDecimal("500.99"));

							}
						}.build());

						addProduct(new ProductBuilder() {
							{
								name("SGS2");
								description("Smartphone");
								price(new BigDecimal("400.99"));
							}
						}.build());
					}
				}.build();

				new OrderBuilder() {
					{
						name("testorder 2");
						date(new Date());
						addProduct(new ProductBuilder() {
							{
								name("Parusso Barbera d'Alba Superiore");
								description("Parusso Barbera d'Alba Superiore - 2010 - Piemonte");
								price(new BigDecimal("30.00"));

							}
						}.build());
					}
				}.build();

				// Customer
				Customer customer = new CustomerBuilder() {
					{
						firstName("Koen");
						lastName("Serneels");
						credentials("koen", "koen");
					}
				}.build();
				customerRepository.saveCustomer(customer);

				TestDataBuilderManager.clear();
				return null;
			}
		});
	}
}