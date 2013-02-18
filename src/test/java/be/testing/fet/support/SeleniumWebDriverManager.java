package be.testing.fet.support;

import java.net.MalformedURLException;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.Proxy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxBinary;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Value;

import com.gargoylesoftware.htmlunit.NicelyResynchronizingAjaxController;
import com.gargoylesoftware.htmlunit.WebClient;

/**
 * @author Koen Serneels
 */
public class SeleniumWebDriverManager implements FactoryBean<WebDriver> {

	@Value("${selenium.browser.name}")
	private String browserName;

	private WebDriver webDriver;

	@Override
	public WebDriver getObject() {
		return webDriver;
	}

	@Override
	public Class<?> getObjectType() {
		return WebDriver.class;
	}

	@Override
	public boolean isSingleton() {
		return true;
	}

	public void createWebDriver() throws MalformedURLException {
		DesiredCapabilities capabilities = new DesiredCapabilities();
		capabilities.setJavascriptEnabled(true);

		switch (browserName) {

		case "firefox":
			Proxy proxy = new Proxy();
			proxy.setProxyType(Proxy.ProxyType.DIRECT);
			capabilities.setCapability(CapabilityType.PROXY, proxy);

			FirefoxBinary firefoxBinary = new FirefoxBinary();
			if ("true".equals(System.getProperty("headlessIT"))) {
				firefoxBinary = new FirefoxBinary();
				firefoxBinary.setEnvironmentProperty("DISPLAY", ":20");
			}
			webDriver = new FirefoxDriver(firefoxBinary, new FirefoxProfile(), capabilities);
			break;

		case "chrome":
			webDriver = new ChromeDriver(capabilities);
			break;

		case "htmlunit":
			webDriver = new HtmlUnitDriver(capabilities) {
				@Override
				protected WebClient modifyWebClient(WebClient client) {
					client.setAjaxController(new NicelyResynchronizingAjaxController());
					return client;
				}
			};
			break;

		default:
			throw new RuntimeException(String.format("Unknown browser type '%s'", browserName));
		}

		// Chrome and HtmlUnit handle AJAX synchronisation properly, no "manual" waiting required
		if (!"chrome".equals(browserName) && !"htmlunit".equals(browserName)) {
			webDriver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
			webDriver.manage().timeouts().setScriptTimeout(5, TimeUnit.SECONDS);
			webDriver.manage().timeouts().pageLoadTimeout(5, TimeUnit.SECONDS);
		}
	}

	public void closeWebDriver() {
		webDriver.quit();
	}
}