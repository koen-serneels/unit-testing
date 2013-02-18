package be.testing.repositories.builders;

import java.util.Date;

import be.testing.entities.Order;
import be.testing.entities.Product;
import be.testing.support.TestDataBuilderManager;

/**
 * @author Koen Serneels
 */
public class OrderBuilder {

	private Order instance = new Order();

	public OrderBuilder name(String name) {
		instance.setName(name);
		return this;
	}

	public OrderBuilder date(Date date) {
		instance.setDate(date);
		return this;
	}

	public OrderBuilder addProduct(Product product, Product... products) {
		instance.getProducts().add(product);
		if (products != null) {
			for (Product p : products) {
				instance.getProducts().add(p);
			}
		}
		return this;
	}

	public Order build() {
		TestDataBuilderManager.save(instance);
		return instance;
	}
}