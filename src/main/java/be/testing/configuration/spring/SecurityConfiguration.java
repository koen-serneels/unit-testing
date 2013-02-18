package be.testing.configuration.spring;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.annotation.Profile;

import be.testing.configuration.spring.profiles.Production;
import be.testing.configuration.spring.profiles.Tomcat;
import be.testing.configuration.spring.profiles.TomcatSelenium;
import be.testing.web.security.TestingUserDetailsService;

/**
 * @author Koen Serneels
 */
@Configuration
@ImportResource("classpath:/spring/spring-security.xml")
@Profile({ Production.name, Tomcat.name, TomcatSelenium.name })
public class SecurityConfiguration {

	@Bean
	public TestingUserDetailsService testingUserDetailsService() {
		return new TestingUserDetailsService();
	}
}
