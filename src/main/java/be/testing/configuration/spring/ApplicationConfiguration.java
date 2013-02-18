package be.testing.configuration.spring;

import java.util.Properties;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @author Koen Serneels
 */
@Configuration
@EnableTransactionManagement
@ComponentScan(basePackages = { "be.testing.configuration", "be.testing.datasetup",
		"be.testing.repositories", "be.testing.services" })
public class ApplicationConfiguration {

	/**
	 * These are the default properties indicating which browser Selenium should start and to which server (host/port)
	 * it should connect to. They are mainly here for Selenium tests ran from the IDE. In case the Selenium tests are
	 * ran by Maven, Maven will set these properties as system properties which will (see
	 * {@link PropertySourcesPlaceholderConfigurer} override the ones configured here.
	 */
	@Bean
	public PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
		PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer = new PropertySourcesPlaceholderConfigurer();
		Properties properties = new Properties();
		properties.setProperty("selenium.server.port", "8080");
		properties.setProperty("selenium.server.name", "localhost");
		properties.setProperty("selenium.browser.name", "firefox");
		propertySourcesPlaceholderConfigurer.setProperties(properties);
		return propertySourcesPlaceholderConfigurer;
	}

	@Bean
	public MessageSource messageSource() {
		ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
		messageSource.setBasenames(new String[] { "messages", "org.springframework.security.messages" });
		messageSource.setUseCodeAsDefaultMessage(true);
		return messageSource;
	}
}