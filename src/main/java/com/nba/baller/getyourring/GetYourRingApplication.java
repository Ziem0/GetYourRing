package com.nba.baller.getyourring;

import com.nba.baller.getyourring.helpers.Position;
import com.nba.baller.getyourring.models.*;
import com.nba.baller.getyourring.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Date;

@SpringBootApplication
public class GetYourRingApplication {

	@Autowired
	OwnerRepo ownerRepo;

	@Autowired
	CityRepo cityRepo;

	@Autowired
	TeamRepo teamRepo;

	@Autowired
	CoachRepo coachRepo;

	@Autowired
	HallRepo hallRepo;

	@Autowired
	MatchRepo matchRepo;

	@Autowired
	PlayerRepo playerRepo;

	@Autowired
	RingRepo ringRepo;




	public static void main(String[] args) {
		SpringApplication.run(GetYourRingApplication.class, args);
	}




	@Bean
	public CommandLineRunner demo() {
		return (args) -> {
			Owner user1 = new Owner("ziemo", "1212", "ziemo@email.pl");
			ownerRepo.save(user1);

			Coach brad_stevens = new Coach("Brad Stevens");
			coachRepo.save(brad_stevens);

			City boston_city = new City("Boston");
			cityRepo.save(boston_city);

			Hall boston_hall = new Hall("Boston hall");
			hallRepo.save(boston_hall);

			Team boston_celtics = new Team("Boston Celtics", boston_hall, boston_city, brad_stevens, user1, 1, 10);
			teamRepo.save(boston_celtics);

			Player kyrie_irving = new Player("Kyrie Irving", Position.PG, boston_celtics);
			playerRepo.save(kyrie_irving);
			Player jason_tatum = new Player("Jason Tatum", Position.SG, boston_celtics);
			playerRepo.save(jason_tatum);
			Player jaylen_brown = new Player("Jaylen Brown", Position.SF, boston_celtics);
			playerRepo.save(jaylen_brown);
			Player gordon_hayward = new Player("Gordon Hayward", Position.PF, boston_celtics);
			playerRepo.save(gordon_hayward);
			Player al_horford = new Player("Al Horford", Position.C, boston_celtics);
			playerRepo.save(al_horford);

			Match match1 = new Match(new Date(), boston_celtics, boston_celtics, 90, 91);
			matchRepo.save(match1);

			Ring ring = new Ring(new Date(), match1, user1);
			ringRepo.save(ring);
		};
	}

}



