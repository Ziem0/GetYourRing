package com.nba.baller.getyourring.services;

import com.nba.baller.getyourring.helpers.Position;
import com.nba.baller.getyourring.models.Owner;
import com.nba.baller.getyourring.models.game.*;
import com.nba.baller.getyourring.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GameService {

	final
	TeamRepo teamRepo;

	final
	PlayerRepo playerRepo;

	final
	CoachRepo coachRepo;

	final
	MatchRepo matchRepo;

	final
	RingRepo ringRepo;

	final
	CityRepo cityRepo;

	final
	HallRepo hallRepo;


	@Autowired
	public GameService(TeamRepo teamRepo, PlayerRepo playerRepo, CoachRepo coachRepo, MatchRepo matchRepo, RingRepo ringRepo, CityRepo cityRepo, HallRepo hallRepo) {
		this.teamRepo = teamRepo;
		this.playerRepo = playerRepo;
		this.coachRepo = coachRepo;
		this.matchRepo = matchRepo;
		this.ringRepo = ringRepo;
		this.cityRepo = cityRepo;
		this.hallRepo = hallRepo;
	}


	public Iterable<Team> getTeamsByOwner(Owner owner) {
		return teamRepo.getTeamsByOwner(owner);
	}

	//add all needed content for new player
	public void addNewGameContent(Owner owner) {

		//BOSTON CELTICS
		Coach brad_stevens = new Coach("Brad Stevens");
		coachRepo.save(brad_stevens);
		City boston_city = new City("Boston");
		cityRepo.save(boston_city);
		Hall td_garden = new Hall("TD Garden");
		hallRepo.save(td_garden);
		Team boston_celtics = new Team("Boston Celtics", td_garden, boston_city, brad_stevens, owner, false);
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

		//UTAH JAZZ
		Coach quin_snyder = new Coach("Quin Snyder");
		coachRepo.save(quin_snyder);
		City utah = new City("Utah");
		cityRepo.save(utah);
		Hall vivint_smart_arena = new Hall("Vivint Smart Arena");
		hallRepo.save(vivint_smart_arena);
		Team utah_jazz = new Team("Utah Jazz", vivint_smart_arena, utah, quin_snyder, owner, false);
		teamRepo.save(utah_jazz);
		Player ricky_rubio = new Player("Ricky Rubio", Position.PG, utah_jazz);
		playerRepo.save(ricky_rubio);
		Player donovan_mitchell = new Player("Donovan Mitchell", Position.SG, utah_jazz);
		playerRepo.save(donovan_mitchell);
		Player joe_ingles = new Player("Joe Ingles", Position.SF, utah_jazz);
		playerRepo.save(joe_ingles);
		Player jae_crowder = new Player("Jae Crowder", Position.PF, utah_jazz);
		playerRepo.save(jae_crowder);
		Player rudy_gobert = new Player("Rudy Gobert", Position.C, utah_jazz);
		playerRepo.save(rudy_gobert);

		//LAL
		Coach luke_walton = new Coach("Luke Walton");
		coachRepo.save(luke_walton);
		City los_angeles = new City("Los Angeles");
		cityRepo.save(los_angeles);
		Hall staples_center = new Hall("Staples Center");
		hallRepo.save(staples_center);
		Team los_angeles_lakers = new Team("Los Angeles Lakers", staples_center, los_angeles, luke_walton, owner, false);
		teamRepo.save(los_angeles_lakers);
		Player lonzo_ball = new Player("Lonzo Ball", Position.PG, los_angeles_lakers);
		playerRepo.save(lonzo_ball);
		Player kyle_kuzma = new Player("Kyle Kuzma", Position.SG, los_angeles_lakers);
		playerRepo.save(kyle_kuzma);
		Player brandon_ingram = new Player("Brandon Ingram", Position.SF, los_angeles_lakers);
		playerRepo.save(brandon_ingram);
		Player lebron_james = new Player("Lebron James", Position.PF, los_angeles_lakers);
		playerRepo.save(lebron_james);
		Player javale_mcGee = new Player("Javale McGee", Position.C, los_angeles_lakers);
		playerRepo.save(javale_mcGee);


		//Houston Rockets
		Coach mike_dantoni = new Coach("Mike D'Antoni");
		coachRepo.save(mike_dantoni);
		City houston = new City("Houston");
		cityRepo.save(houston);
		Hall toyota_center = new Hall("Toyota Center");
		hallRepo.save(toyota_center);
		Team houston_rockets = new Team("Houston Rockets", toyota_center, houston, mike_dantoni, owner, false);
		teamRepo.save(houston_rockets);
		Player chris_paul = new Player("Chris Paul", Position.PG, houston_rockets);
		playerRepo.save(chris_paul);
		Player james_harden = new Player("James Harden", Position.SG, houston_rockets);
		playerRepo.save(james_harden);
		Player eric_gordon = new Player("Eric Gordon", Position.SF, houston_rockets);
		playerRepo.save(eric_gordon);
		Player gerald_green = new Player("Gerald Green", Position.PF, houston_rockets);
		playerRepo.save(gerald_green);
		Player clint_capela = new Player("Clint Capela", Position.C, houston_rockets);
		playerRepo.save(clint_capela);

		//WARRIORS
		Coach steve_kerr = new Coach("Steve Kerr");
		coachRepo.save(steve_kerr);
		City san_francisco = new City("San Francisco");
		cityRepo.save(san_francisco);
		Hall oracle_arena = new Hall("Oracle Arena");
		hallRepo.save(oracle_arena);
		Team golden_state_warriors = new Team("Golden State Warriors", oracle_arena, san_francisco, steve_kerr, owner, false);
		teamRepo.save(golden_state_warriors);
		Player steph_curry = new Player("Steph Curry", Position.PG, golden_state_warriors);
		playerRepo.save(steph_curry);
		Player klay_thompson = new Player("Klay Thompson", Position.SG, golden_state_warriors);
		playerRepo.save(klay_thompson);
		Player kevin_durant = new Player("Kevin Durant", Position.SF, golden_state_warriors);
		playerRepo.save(kevin_durant);
		Player draymond_green = new Player("Draymond Green", Position.PF, golden_state_warriors);
		playerRepo.save(draymond_green);
		Player demarcus_cousins = new Player("Demarcus Cousins", Position.C, golden_state_warriors);
		playerRepo.save(demarcus_cousins);
	}
}
