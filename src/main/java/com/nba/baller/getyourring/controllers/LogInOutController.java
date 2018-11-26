package com.nba.baller.getyourring.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
@RequestMapping("")
public class LogInOutController {

	@GetMapping("{path}")
	public void redirect(HttpServletResponse response, @PathVariable String path) throws IOException {
		response.sendRedirect("/home");
	}

	@GetMapping("/home")
	@ResponseBody
	public String home() {
		//page with link to login page
		return "home";
	}

	//should be on each page as navi
	@GetMapping("/logout")
	public String logoutProcess() {
		return "logout";
	}



//	@GetMapping("/yy")
//	@ResponseBody
//	public String yy(HttpServletRequest request) {
//
//		HttpSession session = request.getSession();
//		return session.getId();
//	}
}
