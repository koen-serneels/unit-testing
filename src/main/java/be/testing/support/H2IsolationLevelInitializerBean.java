package be.testing.support;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;

import be.testing.configuration.spring.profiles.UnitResourceTest;

/**
 * Initializes an H2 database to "read uncommitted" transaction isolation level, note that this is not only for the
 * current connection, but for the entire lifespan of the database (so also for any future connections being made).
 * <p/>
 * <b>ONLY use this for testing purposes!</b>
 * <p/>
 * 
 * For example; when running Selenium tests, multiple database clients can be involved:
 * 
 * <ul>
 * <li>The application running within tomcat will start a H2 database with tcp connector</li>
 * <li>The application will connect itself to its started database
 * <li>The Selenium test will also connect to to the database inside the container to setup data for running the test</li>
 * </ul>
 * 
 * A Selenium test (and each unit test for that matter) should run independently from each other. Meaning that data
 * setup by a test should not be visible to any other test. This means that inserted test data by the test should be
 * removed once the test has finished.
 * <p/>
 * 
 * One strategy to do this is to let the transaction rollback (the default with
 * AbstractTransactionalTestNGSpringContextTests). For a standard {@link UnitResourceTest} where there is one database
 * client (the test itself) there is no issue, since a client can always read the data its inserted/updated inside the
 * same transaction. However, for the scenario described above having 2 clients, the Selenium test will insert/update
 * data inside its transaction, but these modifications will not be visible by the application (2nd client) until the
 * Selenium test actually commits the data. By modifying the transaction isolation level we can however let the second
 * client read the uncommited data from the Selenium client. Hence saving the Selenium test from actually committing and
 * performing any manual database cleanup after the test finished.
 * <p/>
 * 
 * <b>Note:</b> setting the isolation level to read uncommitted is not a guarantee that you will be able to read
 * uncommited data. The database interprets this as; "depending on my concurrency strategy I'm allowed to do
 * optimizations that could result in my client reading uncommited data from others". However, the database is not
 * required to actually let you read that uncommited data, it is a worst case scenario that could occur, you cannot
 * depend on it as a "feature". For example, Oracle uses mutliversioning concurrency control which basically only
 * supports read commited, serializable (and read only). Setting the isolation level to read uncommited means that
 * oracle will provive read commited semantics with the same performance as for read uncommited, which is a good thing.
 * However, in this case we actually rely on the fact that we can read uncommited data. So the moral is that this
 * scenario from depending on being able to read uncommited data might not work with every database you might choose for
 * running your tests against. If it doesn't, you will have to fallback to a strategy that automatically cleans up the
 * database state after a test.
 * 
 * @author Koen Serneels
 */
public class H2IsolationLevelInitializerBean {

	private JdbcTemplate jdbcTemplate;

	public H2IsolationLevelInitializerBean(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
		setIsolationLevelReadUncommited();
	}

	private void setIsolationLevelReadUncommited() {
		jdbcTemplate.execute("SET LOCK_MODE 0");
	}
}