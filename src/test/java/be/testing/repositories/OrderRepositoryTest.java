package be.testing.repositories;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Date;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTransactionalTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import be.testing.configuration.spring.ApplicationConfiguration;
import be.testing.configuration.spring.profiles.UnitResourceTest;
import be.testing.entities.Order;
import be.testing.repositories.OrderRepository;
import be.testing.repositories.OrderSearchCriteria;
import be.testing.repositories.builders.OrderBuilder;
import be.testing.repositories.builders.ProductBuilder;
import be.testing.support.TestDataBuilderManager;

/**
 * @author Koen Serneels
 */
@Test
@ActiveProfiles(UnitResourceTest.name)
@ContextConfiguration(classes = ApplicationConfiguration.class)
public class OrderRepositoryTest extends AbstractTransactionalTestNGSpringContextTests {

	@Autowired
	private OrderRepository orderRepository;

	@PersistenceContext
	private EntityManager entityManager;

	private Date today = new Date();

	@BeforeMethod
	public void dataSetup() {
		TestDataBuilderManager.init(entityManager);
		new OrderBuilder() {
			{
				name("testorder");
				date(today);

				addProduct(new ProductBuilder() {
					{
						name("SGS3");
						description("Better smartphone");
						price(new BigDecimal("500.99"));

					}
				}.build());

				addProduct(new ProductBuilder() {
					{
						name("SGS2");
						description("Good smartphone");
						price(new BigDecimal("400.99"));

					}
				}.build());
			}
		}.build();
	}

	public void testSaveOrder() {
		Order order = new OrderBuilder() {
			{
				name("saveordertest");
				date(today);
			}
		}.build();

		orderRepository.saveOrder(order);

		@SuppressWarnings("unchecked")
		Collection<Order> orders = entityManager.createQuery("from Order").getResultList();

		Assert.assertTrue(orders.size() == 2);

		Order orderToVerify = null;
		for (Order retrievedOrder : orders) {
			if (order.getId().equals(retrievedOrder.getId())) {
				orderToVerify = retrievedOrder;
			}
		}

		Assert.assertNotNull(orderToVerify);

		Assert.assertEquals(orderToVerify.getName(), "saveordertest");
		Assert.assertEquals(orderToVerify.getDate(), today);
		Assert.assertTrue(orderToVerify.getProducts().isEmpty());
	}

	public void testFindOrders() {
		OrderSearchCriteria orderSearchCriteria = new OrderSearchCriteria();

		Collection<Order> orders = orderRepository.findOrders(orderSearchCriteria);
		Assert.assertEquals(orders.size(), 0);

		orderSearchCriteria.setOrderDate(new Date());
		orders = orderRepository.findOrders(orderSearchCriteria);
		Assert.assertEquals(orders.size(), 1);
		Order order = orders.iterator().next();
		Assert.assertEquals(order.getName(), "testorder");
		Assert.assertEquals(order.getDate(), today);
		Assert.assertEquals(order.getProducts().size(), 2);

		orderSearchCriteria.setProductName("SGS2");
		orders = orderRepository.findOrders(orderSearchCriteria);
		Assert.assertEquals(orders.size(), 1);
		order = orders.iterator().next();
		Assert.assertEquals(order.getName(), "testorder");
		Assert.assertEquals(order.getDate(), today);
		Assert.assertEquals(order.getProducts().size(), 2);

		orderSearchCriteria.setProductDescription("Good smartphone");
		orders = orderRepository.findOrders(orderSearchCriteria);
		Assert.assertEquals(orders.size(), 1);
		order = orders.iterator().next();
		Assert.assertEquals(order.getName(), "testorder");
		Assert.assertEquals(order.getDate(), today);
		Assert.assertEquals(order.getProducts().size(), 2);

		orderSearchCriteria.setProductDescription("Better smartphone");
		orders = orderRepository.findOrders(orderSearchCriteria);
		Assert.assertEquals(orders.size(), 0);
	}
}
