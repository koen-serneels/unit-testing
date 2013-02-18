package be.testing.sandbox;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.annotations.Test;

@Test
@ContextConfiguration
public class SandboxTest extends AbstractTestNGSpringContextTests {

	@Autowired
	private String string;

	public void test() {
		Assert.assertEquals(string, "SGS3");
	}

	@Configuration
	static class SandboxTestConfiguration {
		@Bean
		public String imASimpleString() {
			return "SGS3";
		}
	}
}
