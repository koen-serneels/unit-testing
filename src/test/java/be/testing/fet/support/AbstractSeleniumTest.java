package be.testing.fet.support;

import javax.management.MBeanServerConnection;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTransactionalTestNGSpringContextTests;
import org.springframework.transaction.PlatformTransactionManager;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;

import be.testing.configuration.spring.ApplicationConfiguration;
import be.testing.configuration.spring.profiles.UnitResourceTest;
import be.testing.configuration.spring.profiles.UnitSeleniumTest;
import be.testing.repositories.CustomerRepositoryMBean;

/**
 * @author Koen Serneels
 */
@Test(groups = "selenium")
@ActiveProfiles({ UnitResourceTest.name, UnitSeleniumTest.name })
@ContextConfiguration(classes = ApplicationConfiguration.class)
public abstract class AbstractSeleniumTest extends AbstractTransactionalTestNGSpringContextTests {

	// Meant to be used by subclasses
	@PersistenceContext
	protected EntityManager entityManager;
	@Autowired
	protected CustomerRepositoryMBean customerRepository;
	@Autowired
	protected WebDriver driver;
	@Autowired
	private MessageSource messageSource;

	// Private for setup
	@Value("${selenium.server.port}")
	private String serverPort;
	@Value("${selenium.server.name}")
	private String serverHost;
	@Autowired
	private MBeanServerConnection mBeanServerConnection;
	@Autowired
	private PlatformTransactionManager platformTransactionManager;

	@AfterMethod
	public void generalCleanup() {
		customerRepository.clearAll();
	}

	protected void login(String username, String password) {
		navigate("");
		driver.findElement(By.id("username")).sendKeys(username);
		driver.findElement(By.id("password")).sendKeys(password);
		driver.findElement(By.id("login")).click();
	}

	protected String getMessage(String key) {
		return messageSource.getMessage(key, null, null);
	}

	protected void logout() {
		navigate("logout");
	}

	protected abstract String getContextPath();

	protected void navigate(String contextRelativePath) {
		driver.get(String.format("http://%s:%s/%s/%s", serverHost, serverPort, getContextPath(), contextRelativePath));
	}
}