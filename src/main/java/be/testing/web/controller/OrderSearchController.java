package be.testing.web.controller;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import be.testing.entities.Order;
import be.testing.repositories.OrderSearchCriteria;
import be.testing.services.OrderService;

/**
 * @author Koen Serneels
 */
@Controller
public class OrderSearchController {

	@Autowired
	private OrderService orderService;

	@RequestMapping(value = "/secured/searchOrders.htm", method = RequestMethod.GET)
	public String showSearchOrders() {
		return "secured/searchorders";
	}

	@RequestMapping(value = "/secured/searchOrders.json", method = RequestMethod.GET)
	public @ResponseBody
	Collection<Order> searchOrders(OrderSearchCriteria orderSearchCriteria) {
		return orderService.findOrders(orderSearchCriteria);
	}
}
