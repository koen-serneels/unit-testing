package be.testing.web.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.testng.annotations.Test;

import be.testing.web.controller.LoginController;

/**
 * @author Koen Serneels
 */
@Test
public class LoginControllerTest {

	public void testShowLogin() throws Exception {
		LoginController loginController = new LoginController();
		MockMvc mockMvc = MockMvcBuilders.standaloneSetup(loginController).build();
		mockMvc.perform(get("/public/login.htm")).andExpect(status().isOk()).andExpect(view().name("public/login"));
	}
}
