package be.testing.services;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import be.testing.entities.Order;
import be.testing.repositories.OrderRepository;
import be.testing.repositories.OrderSearchCriteria;

/**
 * @author Koen Serneels
 */
@Service
@Transactional
public class OrderServiceImpl implements OrderService {

	@Autowired
	private OrderRepository orderRepository;

	@Override
	public void saveOrder(Order order) {
		orderRepository.saveOrder(order);
	}

	@Override
	public Collection<Order> findOrders(OrderSearchCriteria orderSearchCriteria) {
		return orderRepository.findOrders(orderSearchCriteria);
	}
}
