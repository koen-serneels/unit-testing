package be.testing.configuration.spring;

import java.sql.SQLException;

import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSource;
import org.h2.Driver;
import org.h2.tools.Server;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.jndi.JndiObjectFactoryBean;

import be.testing.configuration.spring.profiles.Production;
import be.testing.configuration.spring.profiles.Tomcat;
import be.testing.configuration.spring.profiles.TomcatSelenium;
import be.testing.configuration.spring.profiles.UnitResourceTest;
import be.testing.configuration.spring.profiles.UnitResourceTestDebug;
import be.testing.configuration.spring.profiles.UnitSeleniumTest;
import be.testing.support.H2IsolationLevelInitializerBean;

/**
 * @author Koen Serneels
 */
@Configuration
public class DatabaseConfiguration {

	@Configuration
	@Production
	static class OracleDatabaseWithConnector {
		@Bean
		public JndiObjectFactoryBean dataSource() {
			// Production profile not used in this app, but demonstration how production datasource lookup could be
			JndiObjectFactoryBean jndiObjectFactoryBean = new JndiObjectFactoryBean();
			jndiObjectFactoryBean.setJndiName("jdbc/myOracleDb");
			return jndiObjectFactoryBean;
		}
	}

	@Configuration
	@Profile({ "!" + Production.name })
	static class H2IsolationLevelInitializer {
		@Autowired
		private DataSource dataSource;

		@Bean
		public H2IsolationLevelInitializerBean h2IsolationLevelInitializerBean() {
			return new H2IsolationLevelInitializerBean(dataSource);
		}
	}

	@Configuration
	@Profile({ Tomcat.name, UnitResourceTest.name })
	static class H2DatabaseEmbedded {
		@Bean
		public EmbeddedDatabase embeddedDatabase() {
			EmbeddedDatabaseBuilder builder = new EmbeddedDatabaseBuilder();
			builder.setType(EmbeddedDatabaseType.H2);
			return builder.build();
		}
	}

	@Configuration
	@Profile({ UnitSeleniumTest.name })
	static class H2RemoteConnection {
		@Bean
		@DependsOn("embeddedDatabase")
		public DataSource dataSource() {
			BasicDataSource basicDataSource = new BasicDataSource();
			basicDataSource.setDriverClassName(Driver.class.getName());
			basicDataSource.setUrl("jdbc:h2:tcp://localhost/mem:db");
			basicDataSource.setUsername("sa");
			basicDataSource.setPassword("");
			return basicDataSource;
		}
	}

	@Configuration
	@Profile({ TomcatSelenium.name, UnitResourceTestDebug.name })
	static class H2DatabaseWithConnector {
		@Bean(initMethod = "start", destroyMethod = "stop")
		public Server embeddedDatabase() throws SQLException {
			return Server.createTcpServer("-tcp", "-tcpAllowOthers", "-tcpShutdownForce");
		}

		@Bean
		@DependsOn("embeddedDatabase")
		public DataSource dataSource() {
			BasicDataSource basicDataSource = new BasicDataSource();
			basicDataSource.setDriverClassName(Driver.class.getName());
			basicDataSource.setUrl("jdbc:h2:tcp://localhost/mem:db");
			basicDataSource.setUsername("sa");
			basicDataSource.setPassword("");
			return basicDataSource;
		}
	}
}
