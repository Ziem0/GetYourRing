package com.nba.baller.getyourring;

import com.nba.baller.getyourring.models.Owner;
import com.nba.baller.getyourring.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class GetYourRingApplication {

	@Autowired
	UserService userService;

	public static void main(String[] args) {
		SpringApplication.run(GetYourRingApplication.class, args);
	}


	@Bean
	public CommandLineRunner demo() {
		return (args) -> {

			Owner admin = new Owner("ziemo", "1212", "andrzejewski.ziemowit@gmail.com");
			userService.saveAdmin(admin);
		};
	}


}



