package be.testing.configuration.spring;

import java.util.Properties;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.hibernate.dialect.H2Dialect;
import org.hibernate.dialect.Oracle10gDialect;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

import be.testing.configuration.spring.profiles.Production;
import be.testing.configuration.spring.profiles.Tomcat;
import be.testing.configuration.spring.profiles.TomcatSelenium;
import be.testing.configuration.spring.profiles.UnitResourceTest;
import be.testing.configuration.spring.profiles.UnitSeleniumTest;

/**
 * @author Koen Serneels
 */
@Configuration
public class JpaConfiguration {

	@Autowired
	@Qualifier("jpa.provider.properties")
	private Properties properties;

	@Autowired
	private DataSource dataSource;

	@Bean
	public JpaVendorAdapter jpaVendorAdapter() {
		return new HibernateJpaVendorAdapter();
	}

	@Bean
	public FactoryBean<EntityManagerFactory> entityManagerFactory() {
		LocalContainerEntityManagerFactoryBean localContainerEntityManagerFactoryBean = new LocalContainerEntityManagerFactoryBean();
		localContainerEntityManagerFactoryBean.setJpaProperties(properties);
		localContainerEntityManagerFactoryBean.setDataSource(dataSource);
		localContainerEntityManagerFactoryBean.setJpaVendorAdapter(jpaVendorAdapter());
		return localContainerEntityManagerFactoryBean;
	}

	@Configuration
	@Profile({ Tomcat.name, UnitResourceTest.name, UnitSeleniumTest.name, TomcatSelenium.name })
	static class JpaProviderH2Properties {
		@Bean(name = "jpa.provider.properties")
		public Properties properties() {
			Properties properties = new Properties();
			properties.setProperty("hibernate.dialect", H2Dialect.class.getName());
			properties.setProperty("hibernate.hbm2ddl.auto", "update");
			return properties;
		}
	}

	@Configuration
	@Production
	static class JpaProviderOracleProperties {
		@Bean(name = "jpa.provider.properties")
		public Properties properties() {
			Properties properties = new Properties();
			properties.setProperty("hibernate.dialect", Oracle10gDialect.class.getName());
			// Probably some other things you need in production
			return properties;
		}
	}
}
