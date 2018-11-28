package com.nba.baller.getyourring.controllers;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("")
public class AdminRestController {

	@GetMapping("/admin")
	public String admin(@RequestParam(value = "owners", required = false) String owners) {
		//create owners statistics concerning:
		//json with all owners
		return "owners";   //switch on html
	}

	//different values methods
}
