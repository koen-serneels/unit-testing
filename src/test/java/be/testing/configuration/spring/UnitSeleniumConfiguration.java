package be.testing.configuration.spring;

import java.net.MalformedURLException;

import javax.management.MBeanServerConnection;
import javax.management.MalformedObjectNameException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jmx.access.MBeanProxyFactoryBean;
import org.springframework.jmx.support.MBeanServerConnectionFactoryBean;

import be.testing.configuration.spring.TomcatSeleniumConfiguration;
import be.testing.configuration.spring.profiles.UnitSeleniumTest;
import be.testing.fet.support.SeleniumWebDriverManager;
import be.testing.repositories.CustomerRepositoryMBean;
import be.testing.repositories.CustomerRepositoryStub;

/**
 * @author Koen Serneels
 */
@Configuration
@UnitSeleniumTest
public class UnitSeleniumConfiguration {

	@Autowired
	@Qualifier("mBeanServerConnectionFactoryBean")
	private MBeanServerConnection mBeanServerConnection;

	@Bean
	public MBeanServerConnectionFactoryBean mBeanServerConnectionFactoryBean() throws MalformedURLException {
		MBeanServerConnectionFactoryBean mBeanServerConnectionFactoryBean = new MBeanServerConnectionFactoryBean();
		mBeanServerConnectionFactoryBean.setServiceUrl(TomcatSeleniumConfiguration.JMX_URL);
		return mBeanServerConnectionFactoryBean;
	}

	@Bean
	public MBeanProxyFactoryBean mBeanProxyFactoryBean() throws MalformedObjectNameException {
		MBeanProxyFactoryBean mBeanProxyFactoryBean = new MBeanProxyFactoryBean();
		mBeanProxyFactoryBean.setObjectName("testing:name=" + CustomerRepositoryStub.class.getSimpleName());
		mBeanProxyFactoryBean.setProxyInterface(CustomerRepositoryMBean.class);
		mBeanProxyFactoryBean.setServer(mBeanServerConnection);
		return mBeanProxyFactoryBean;
	}

	@Bean(initMethod = "createWebDriver", destroyMethod = "closeWebDriver")
	public SeleniumWebDriverManager seleniumWebDriverManager() {
		return new SeleniumWebDriverManager();
	}
}
