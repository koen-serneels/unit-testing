package be.testing.configuration.spring;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.EnableMBeanExport;
import org.springframework.jmx.support.ConnectorServerFactoryBean;
import org.springframework.remoting.rmi.RmiRegistryFactoryBean;

import be.testing.configuration.spring.profiles.TomcatSelenium;

/**
 * @author Koen Serneels
 */
@Configuration
@TomcatSelenium
@EnableMBeanExport(defaultDomain = "testing")
public class TomcatSeleniumConfiguration {

	public static String JMX_URL = "service:jmx:rmi:///jndi/rmi://localhost:13000/jmxrmi";

	@Bean
	public RmiRegistryFactoryBean rmiRegistry() {
		RmiRegistryFactoryBean rmiRegistryFactoryBean = new RmiRegistryFactoryBean();
		rmiRegistryFactoryBean.setPort(13000);
		return rmiRegistryFactoryBean;
	}

	@Bean
	@DependsOn("rmiRegistry")
	public ConnectorServerFactoryBean connectorServerFactoryBean() {
		ConnectorServerFactoryBean connectorServerFactoryBean = new ConnectorServerFactoryBean();
		connectorServerFactoryBean.setServiceUrl(JMX_URL);
		return connectorServerFactoryBean;
	}
}
