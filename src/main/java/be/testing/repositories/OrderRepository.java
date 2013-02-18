package be.testing.repositories;

import java.util.Collection;

import be.testing.entities.Order;

/**
 * @author Koen Serneels
 */
public interface OrderRepository {

	void saveOrder(Order order);

	Collection<Order> findOrders(OrderSearchCriteria orderSearchCriteria);
}
