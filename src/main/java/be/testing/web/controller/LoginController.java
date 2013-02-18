package be.testing.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @author Koen Serneels
 */
@Controller
public class LoginController {

	@RequestMapping(value = "/public/login.htm", method = RequestMethod.GET)
	public String showLogin() {
		return "public/login";
	}
}
