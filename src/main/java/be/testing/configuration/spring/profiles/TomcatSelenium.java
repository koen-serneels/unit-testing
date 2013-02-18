package be.testing.configuration.spring.profiles;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.context.annotation.Profile;

/**
 * @author Koen Serneels
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Profile(TomcatSelenium.name)
public @interface TomcatSelenium {

	public static String name = "tomcat-selenium";
}
