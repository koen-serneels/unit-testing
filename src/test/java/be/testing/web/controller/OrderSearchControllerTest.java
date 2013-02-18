package be.testing.web.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.text.SimpleDateFormat;

import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor;
import org.springframework.context.support.StaticApplicationContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.testng.Assert;
import org.testng.annotations.Test;

import be.testing.repositories.OrderSearchCriteria;
import be.testing.services.OrderService;
import be.testing.web.controller.OrderSearchController;

/**
 * @author Koen Serneels
 */
@Test
public class OrderSearchControllerTest {

	public void testShowSearchOrders() throws Exception {
		OrderSearchController orderSearchController = new OrderSearchController();
		MockMvc mockMvc = MockMvcBuilders.standaloneSetup(orderSearchController).build();
		mockMvc.perform(get("/secured/searchOrders.htm")).andExpect(status().isOk())
		.andExpect(view().name("secured/searchorders"));
	}

	public void testSearchOrders() throws Exception {
		try (StaticApplicationContext applicationContext = new StaticApplicationContext()) {
			applicationContext.registerSingleton("autowiredAnnotationBeanPostProcessor",
					AutowiredAnnotationBeanPostProcessor.class);
			OrderService orderService = Mockito.mock(OrderService.class);
			applicationContext.getDefaultListableBeanFactory().registerSingleton("orderService", orderService);
			applicationContext.registerSingleton("orderSearchController", OrderSearchController.class);
			applicationContext.refresh();

			MockMvc mockMvc = MockMvcBuilders.standaloneSetup(applicationContext.getBean(OrderSearchController.class))
					.build();
			mockMvc.perform(
					get("/secured/searchOrders.json").param("productName", "SGS2")
					.param("productDescription", "Good smartphone").param("orderDate", "01012000")).andExpect(
							status().isOk());

			ArgumentCaptor<OrderSearchCriteria> argument = ArgumentCaptor.forClass(OrderSearchCriteria.class);
			Mockito.verify(orderService).findOrders(argument.capture());

			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
			Assert.assertEquals(argument.getValue().getProductName(), "SGS2");
			Assert.assertEquals(argument.getValue().getProductDescription(), "Good smartphone");
			Assert.assertEquals(simpleDateFormat.format(argument.getValue().getOrderDate()), "01/01/2000");
		}
	}
}
