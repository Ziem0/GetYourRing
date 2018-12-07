package com.nba.baller.getyourring;

import com.nba.baller.getyourring.helpers.Role;
import com.nba.baller.getyourring.models.Owner;
import com.nba.baller.getyourring.models.Roles;
import com.nba.baller.getyourring.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class GetYourRingApplication {

	@Autowired
	OwnerRepo ownerRepo;

	@Autowired
	RoleRepo roleRepo;

	public static void main(String[] args) {
		SpringApplication.run(GetYourRingApplication.class, args);
	}


	@Bean
	public CommandLineRunner demo() {
		return (args) -> {

			Owner admin = new Owner("ziemo", "1212", "andrzejewski.ziemowit@gmail.com");
			ownerRepo.save(admin);
			Roles role1 = new Roles(admin, Role.ROLE_ADMIN);
			roleRepo.save(role1);
		};
	}


}



