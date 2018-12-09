package com.nba.baller.getyourring.controllers;

import com.nba.baller.getyourring.models.Owner;
import com.nba.baller.getyourring.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;


@RestController
@RequestMapping("/")
public class AdminRestController {

	@Autowired
	UserService userService;


	@GetMapping("/admin")
	public String welcome() {
		return "hello admin!";
	}

	@GetMapping("/admin/getallusers")
	public Iterable<Owner> showAllUsers() {
		return userService.getAllOwners();
	}

	@GetMapping("/admin/getuserby")
	public Owner showUserByUsername(@RequestParam(value = "username", required = true) String ownerId) {
		Optional<Owner> user = userService.getUserById(ownerId);
		return user.orElse(null);
	}

}
