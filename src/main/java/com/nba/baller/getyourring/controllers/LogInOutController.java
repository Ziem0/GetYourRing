package com.nba.baller.getyourring.controllers;

import com.nba.baller.getyourring.models.Owner;
import com.nba.baller.getyourring.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
@RequestMapping("/")
public class LogInOutController {

	@Autowired
	UserService userService;


	@GetMapping("/")
	public void redirect1(HttpServletResponse response) throws IOException {
		response.sendRedirect("/home");
	}

	@GetMapping("{path}")
	public void redirect2(HttpServletResponse response, @PathVariable String path) throws IOException {
		response.sendRedirect("/home");
	}

	/**
	 * page with links to:
	 * -login page
	 * -create new account page
	 * create new account only in case user is logged out
	 *
	 * @return
	 */
	@GetMapping("/home")
	public String home(HttpServletRequest request, ModelMap map) {

		boolean isSessionExpired = userService.isSessionExpired(request);

		map.addAttribute("isSessionExpired", isSessionExpired);

		return "home";
	}


	//create account and redirect to login page
	@GetMapping("/create")
	public String createAccount(@ModelAttribute Owner owner) {
		return "create";
	}

	//create account and redirect to login page
	@PostMapping("/create")
	public void postCreateAccount(Owner owner, HttpServletResponse response) throws IOException {
		userService.saveOwner(owner);
		response.sendRedirect("/game");
	}

	@GetMapping("/login")
	public String logging(@ModelAttribute Owner owner,
	                      Model model,
	                      @RequestParam(value = "error", required = false)String errorOrNull) {

		if (errorOrNull == null) {
			model.addAttribute("message", "ok");
		}

		return "login";
	}

}
