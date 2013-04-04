package be.testing.repositories;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Date;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.hibernate.Session;
import org.hibernate.StatelessSession;
import org.hibernate.ejb.HibernateEntityManagerFactory;
import org.hibernate.jdbc.Work;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTransactionalTestNGSpringContextTests;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;
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
	@Autowired
	private PlatformTransactionManager txManager;

	@PersistenceContext
	private EntityManager entityManager;
	@Autowired
	private HibernateEntityManagerFactory entityManagerFactory;

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

	public void testStatelessSession() {
		@SuppressWarnings("unchecked")
		Collection<Order> orders = statelessSession.createQuery("from Order").list();

		Assert.assertTrue(orders.size() == 1);

		Order order = orders.iterator().next();

		order.setName("update from statelesssession");
		statelessSession.update(order);
	}

	public void testStatelessBulkProcess() {

		// Executing update in new transaction
		new TransactionTemplate(txManager).execute(new TransactionCallback<Void>() {
			@Override
			public Void doInTransaction(TransactionStatus status) {
				entityManager.unwrap(Session.class).doWork(new Work() {
					@Override
					public void execute(Connection connection) throws SQLException {
						StatelessSession innerStatelessSession = entityManagerFactory.getSessionFactory()
								.openStatelessSession(connection);
						try {
							Order order = (Order) innerStatelessSession.createQuery("from Order where id =1")
									.uniqueResult();
							order.setName("testing 123");
							innerStatelessSession.update(order);
						} finally {
							innerStatelessSession.close();
						}
					}
				});
				return null;
			}
		});

		// Verifying result
		@SuppressWarnings("unchecked")
		Collection<Order> orders = statelessSession.createQuery("from Order").list();

		Assert.assertTrue(orders.size() == 1);

		Order order = orders.iterator().next();
		Assert.assertEquals(order.getName(), "testing 123");
	}
}