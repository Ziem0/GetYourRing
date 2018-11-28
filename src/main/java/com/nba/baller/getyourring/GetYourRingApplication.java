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

			Owner owner1 = new Owner("ziemo", "1212", "ziemo@email.pl");
			ownerRepo.save(owner1);
			Roles role1 = new Roles(owner1, Role.ROLE_USER);
			roleRepo.save(role1);
			Owner owner2 = new Owner("nina", "1212", "nina@email.pl");
			ownerRepo.save(owner2);
			Roles role2 = new Roles(owner2, Role.ROLE_ADMIN);
			roleRepo.save(role2);

//			Match match1 = new Match(new Date(), boston_celtics, boston_celtics, 90, 91);
//			matchRepo.save(match1);
//
//			Ring ring = new Ring(new Date(), match1, owner1);
//			ringRepo.save(ring);
		};
	}


}



