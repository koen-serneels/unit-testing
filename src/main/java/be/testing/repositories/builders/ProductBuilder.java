package be.testing.repositories.builders;

import java.math.BigDecimal;

import be.testing.entities.Product;
import be.testing.support.TestDataBuilderManager;

/**
 * @author Koen Serneels
 */
public class ProductBuilder {

	private Product instance = new Product();

	public ProductBuilder name(String name) {
		instance.setName(name);
		return this;
	}

	public ProductBuilder description(String description) {
		instance.setDescription(description);
		return this;
	}

	public ProductBuilder price(BigDecimal price) {
		instance.setPrice(price);
		return this;
	}

	public Product build() {
		TestDataBuilderManager.save(instance);
		return instance;
	}
}
