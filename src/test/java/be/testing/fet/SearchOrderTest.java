package be.testing.fet;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.openqa.selenium.By;
import org.springframework.beans.factory.annotation.Autowired;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import be.testing.entities.Customer;
import be.testing.fet.support.AbstractSeleniumTest;
import be.testing.repositories.OrderRepository;
import be.testing.repositories.builders.CustomerBuilder;
import be.testing.repositories.builders.OrderBuilder;
import be.testing.repositories.builders.ProductBuilder;
import be.testing.support.TestDataBuilderManager;

/**
 * @author Koen Serneels
 */
@Test(groups = "selenium")
public class SearchOrderTest extends AbstractSeleniumTest {

	@Autowired
	private OrderRepository orderRepository;

	@BeforeMethod
	public void dataSetup() {
		TestDataBuilderManager.init(entityManager);
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

		Customer customer = new CustomerBuilder() {
			{
				firstName("Koen");
				lastName("Serneels");
				credentials("koen", "koen");
			}
		}.build();
		customerRepository.saveCustomer(customer);
		entityManager.flush();
	}

	public void testSearchOrders() throws Exception {
		login("koen", "koen");
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
		Date today = new Date();

		driver.findElement(By.id("orderdate")).sendKeys(simpleDateFormat.format(today));
		driver.findElement(By.id("searchorders")).click();

		// BEWARE: Assert.assertTrue(driver.findElement(By.id("orders")).getText().contains("testorder"));
		//
		// The above will not (always) work because of timing issues. When using firefox, there is a default
		// wait time for elements that do not exist. However, the "orders" table *does* already exist on the page
		// it is the table data that doesn't exist until the AJAX call returned. Below is a more find grained version
		// which actually gets the table row. if the table row is not immediately present because the AJAX call didn't
		// return yet, Selenium will wait automatically up to the maximum configured wait time.

		String firstRow = driver.findElement(By.cssSelector("#orders tbody tr")).getText();
		Assert.assertTrue(firstRow.contains("testorder"));
		Assert.assertTrue(firstRow.contains(simpleDateFormat.format(today)));

		logout();
	}

	@Override
	protected String getContextPath() {
		return "testing";
	}
}
