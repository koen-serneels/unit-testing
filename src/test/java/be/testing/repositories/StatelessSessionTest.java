package be.testing.repositories;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Date;

import org.hibernate.StatelessSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTransactionalTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import be.testing.configuration.spring.ApplicationConfiguration;
import be.testing.configuration.spring.profiles.UnitResourceTestDebug;
import be.testing.entities.Order;
import be.testing.entities.Product;
import be.testing.repositories.builders.OrderBuilder;
import be.testing.repositories.builders.ProductBuilder;

/**
 * @author Koen Serneels
 */
@Test
@ActiveProfiles(UnitResourceTestDebug.name)
@ContextConfiguration(classes = ApplicationConfiguration.class)
public class StatelessSessionTest extends AbstractTransactionalTestNGSpringContextTests {

	@Autowired
	private OrderRepository orderRepository;

	@Autowired
	private StatelessSession statelessSession;

	private Date today = new Date();

	@BeforeMethod
	public void dataSetup() {

		statelessSession.insert(new OrderBuilder() {
			{
				name("testorder");
				date(today);

				Product product = new ProductBuilder() {
					{
						name("SGS3");
						description("Better smartphone");
						price(new BigDecimal("500.99"));

					}
				}.build();
				statelessSession.insert(product);
				addProduct(product);

				product = new ProductBuilder() {
					{
						name("SGS2");
						description("Good smartphone");
						price(new BigDecimal("400.99"));

					}
				}.build();
				statelessSession.insert(product);
				addProduct(product);

			}
		}.build());
	}

	@Rollback(false)
	public void testStatelessSession() {
		@SuppressWarnings("unchecked")
		Collection<Order> orders = statelessSession.createQuery("from Order").list();

		Assert.assertTrue(orders.size() == 1);

		Order order = orders.iterator().next();

		order.setName("update from statelesssession");
		statelessSession.update(order);
	}
}