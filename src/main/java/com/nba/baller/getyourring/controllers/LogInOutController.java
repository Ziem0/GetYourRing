package com.nba.baller.getyourring.controllers;

import com.nba.baller.getyourring.models.Owner;
import com.nba.baller.getyourring.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
@RequestMapping("")
public class LogInOutController {

	@Autowired
	UserService userService;

	@GetMapping("{path}")
	public void redirect(HttpServletResponse response, @PathVariable String path) throws IOException {
		response.sendRedirect("/home");
	}

	@GetMapping("/home")
	public String home() {
		//page with link to login page
		//page with link to create page
		return "home";
	}

	@GetMapping("/create")
	public String createAccount(@ModelAttribute Owner owner) {
		//create account and redirect to login page
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

	//should be on each page as navi
	@GetMapping("/logout")
	public String logoutProcess() {
		return "logout";
	}


}
