package be.testing.services;

import java.util.Collection;

import be.testing.entities.Order;
import be.testing.repositories.OrderSearchCriteria;

/**
 * @author Koen Serneels
 */
public interface OrderService {

	void saveOrder(Order order);

	Collection<Order> findOrders(OrderSearchCriteria orderSearchCriteria);
}
