package com.nba.baller.getyourring.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "")
public class MainController {

	@GetMapping("/start")
	public String demo() {
		return "demo";
	}
}
