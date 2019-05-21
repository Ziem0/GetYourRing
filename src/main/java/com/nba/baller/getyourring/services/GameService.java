package com.nba.baller.getyourring.services;

import com.nba.baller.getyourring.helpers.HandleComparator;
import com.nba.baller.getyourring.helpers.Position;
import com.nba.baller.getyourring.models.Owner;
import com.nba.baller.getyourring.models.game.*;
import com.nba.baller.getyourring.repositories.*;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.time.LocalDate;
import java.util.*;

@Service
@Getter
@Setter
@Slf4j
public class GameService {

	private Owner owner;

	private boolean isOwnerNew;

	private Team userTeam;

	private List<Team> teams;

	private List<Match> nextRoundMatches;

	private boolean isPageReload = false;

	private Integer season;

	private int tradesCounter;

	private String tradeInfo;

	final
	UserService userService;

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
	public GameService(TeamRepo teamRepo, PlayerRepo playerRepo, CoachRepo coachRepo, MatchRepo matchRepo, RingRepo ringRepo, CityRepo cityRepo, HallRepo hallRepo, UserService userService) {
		this.teamRepo = teamRepo;
		this.playerRepo = playerRepo;
		this.coachRepo = coachRepo;
		this.matchRepo = matchRepo;
		this.ringRepo = ringRepo;
		this.cityRepo = cityRepo;
		this.hallRepo = hallRepo;
		this.userService = userService;
	}


	//connected directly to @Repository

	private List<Team> getTeamsByOwner(Owner owner) {
		teams = teamRepo.getTeamsByOwner(owner);
		return teamRepo.getTeamsByOwner(owner);
	}

	private void updateUserTeam(Team userTeam) {
		log.warn("updateTeam");
		teamRepo.save(userTeam);
	}

	private List<Player> getPlayersByTeam(Team team) {
		List<Player> players = playerRepo.getPlayersByTeam(team);
		players.sort(HandleComparator.comparePlayersByPosition());
		return players;
	}

	private Coach getCoachByTeam(Team team) {
		return coachRepo.getCoachByTeam(team.getCoach());
	}

	private void saveTeam(Team team) {
		log.warn("saveTeam");
		teamRepo.save(team);
	}

	private Integer getMaxLeftOpponentsFromAllTeams(Owner owner) {
		Page<Team> maxLeftOpponentsFromAllTeams = teamRepo.getMaxLeftOpponentsFromAllTeams(PageRequest.of(0, 1), owner);
		return maxLeftOpponentsFromAllTeams.iterator().next().getLeftOpponents().size();
	}

	private void addRing(Owner owner, Team team, Integer season) {
		ringRepo.save(new Ring(owner, team.getName(), season));
	}

	private void saveMatch(Match match) {
		log.warn("saveMatch");
		matchRepo.save(match);
	}

	private Player getMvp(Owner owner) {
		Page<Player> mvp = teamRepo.getMvp(PageRequest.of(0, 1), owner);
		return mvp.iterator().next();
	}

	private List<Ring> getRingsByOwner(Owner owner) {
		return ringRepo.getRingsByOwner(owner);
	}

	private Optional<Player> getPlayerById(Integer id) {
		return playerRepo.findById(id);
	}

	private void savePlayer(Player player) {
		log.warn("savePlayer");
		playerRepo.save(player);
	}

	private List<Object[]> getTop10Data() {
		return ringRepo.getTop10Data();
	}

	private Team getOwnerTeam() {
		return teamRepo.findById(userTeam.getId()).get();
	}

	//init all needed items for new player

	private void addNewGameContent(Owner owner) {

		//BOSTON CELTICS
		Coach brad_stevens = new Coach("Brad Stevens");
		coachRepo.save(brad_stevens);
		City boston_city = new City("Boston");
		cityRepo.save(boston_city);
		Hall td_garden = new Hall("TD Garden");
		hallRepo.save(td_garden);
		Team boston_celtics = new Team("Boston Celtics", td_garden, boston_city, brad_stevens, owner);
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
		Team utah_jazz = new Team("Utah Jazz", vivint_smart_arena, utah, quin_snyder, owner);
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
		Team los_angeles_lakers = new Team("Los Angeles Lakers", staples_center, los_angeles, luke_walton, owner);
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
		Team houston_rockets = new Team("Houston Rockets", toyota_center, houston, mike_dantoni, owner);
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
		Team golden_state_warriors = new Team("Golden State Warriors", oracle_arena, san_francisco, steve_kerr, owner);
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

		//SAN ANTONIO SPURS
		Coach gregg_popovic = new Coach("Gregg Popovic");
		coachRepo.save(gregg_popovic);
		City san_antonio = new City("San Antonio");
		cityRepo.save(san_antonio);
		Hall att_center = new Hall("AT&T Center");
		hallRepo.save(att_center);
		Team san_antonio_spurs = new Team("San Antonio Spurs", att_center, san_antonio, gregg_popovic, owner);
		teamRepo.save(san_antonio_spurs);
		Player patty_mills = new Player("Patty Mills", Position.PG, san_antonio_spurs);
		playerRepo.save(patty_mills);
		Player marco_belinelli = new Player("Marco Belinelli", Position.SG, san_antonio_spurs);
		playerRepo.save(marco_belinelli);
		Player demar_deRozan = new Player("Demar DeRozan", Position.SF, san_antonio_spurs);
		playerRepo.save(demar_deRozan);
		Player pau_gasol = new Player("Pau Gasol", Position.PF, san_antonio_spurs);
		playerRepo.save(pau_gasol);
		Player lamarcus_aldridge = new Player("Lamarcus Aldridge", Position.C, san_antonio_spurs);
		playerRepo.save(lamarcus_aldridge);
	}


	//used directly by GameController

	public void initGameHandler(HttpServletRequest request) {

		nextRoundMatches = new LinkedList<>();

		isOwnerNew = false;

		setOwner(getOwner(request));

		setTeams(getTeamsByOwner(owner));


		if (!teams.iterator().hasNext()) {
			addNewGameContent(owner);
			setTeams(getTeamsByOwner(owner));
			isOwnerNew = true;

		} else {
			setTeamForOldUser();
			setNextRoundMatches();
		}
	}

	public void chooseTeamInit(Model model) {

		List<String> teamsNames = new ArrayList<>();
		teams.forEach(team -> teamsNames.add(team.getName()));
		//intro.html display teams to choose based on 'isOwnerNew'
		model.addAttribute("isOwnerNew", isOwnerNew);
		model.addAttribute("teams", teamsNames);
	}

	public void getUserTeamInit(String team) {

		log.warn("getUserTeamInit");

		season = LocalDate.now().getYear()-1;

		setTeamForNewUser(team);

		updateUserTeam(userTeam);

		setNextRoundMatches();

		isOwnerNew = false;
	}

	public void getTeamPageInit(ModelMap map) {

		map.addAttribute("season", season);

		StringBuilder cityPath = new StringBuilder();
		cityPath
				.append(userTeam.getName().substring(0,3))
				.append("/city.jpeg");
		map.addAttribute("cityPath", cityPath);

		List<Player> players = getPlayersByTeam(userTeam);

		Coach coach = getCoachByTeam(userTeam);

		map.addAttribute("tradeInfo", tradeInfo);
		map.addAttribute("tradesCounter", tradesCounter);

		map.addAttribute("players", players);
		map.addAttribute("coach", coach);
		map.addAttribute("city", userTeam.getCity().getName());
		map.addAttribute("hall", userTeam.getHall().getName());
		map.addAttribute("wins", getOwnerTeam().getWins());
		map.addAttribute("balance", getOwnerTeam().getPlusMinus());
		map.addAttribute("matches", nextRoundMatches);
	}

	public void getMatchesInit(ModelMap modelMap) {

		modelMap.addAttribute("nextRoundMatches", nextRoundMatches);

		int matchCounter = 0;

		for (Match match : nextRoundMatches) {

			//reset scoring as a security against hacking(refreshing page)
			match.setTeamOneScore(0);
			match.setTeamTwoScore(0);
			saveMatch(match);

			Team team1 = match.getTeamOne();
			Team team2 = match.getTeamTwo();
			modelMap.addAttribute("teamHome" + matchCounter, team1.getName());
			modelMap.addAttribute("teamAway" + matchCounter, team2.getName());

			String city = team1.getCity().getName();
			String hall = team1.getHall().getName();
			modelMap.addAttribute("city" + matchCounter, city);
			modelMap.addAttribute("hall" + matchCounter, hall);

			List<Player> playersByTeam1 = getPlayersByTeam(team1);
			List<Player> playersByTeam2 = getPlayersByTeam(team2);

			Integer teamOneScore = match.getTeamOneScore();
			Integer teamTwoScore = match.getTeamTwoScore();

			Coach teamOneCoach = team1.getCoach();
			Coach teamTwoCoach = team2.getCoach();

			boolean isHomeBoost = team1.getHall().isCrowdFactorHome();
			int crowdFactor = team1.getHall().getCrowdFactor();
			boolean isOvertimeCrowdFactorHome = team1.getHall().isOvertimeCrowdFactorHome();
			int overtimeBooster = team1.getHall().getOvertimeScoreBooster();

			for (int i = 0; i < playersByTeam1.size(); i++) {

				Player playerHome = playersByTeam1.get(i);
				Player playerAway = playersByTeam2.get(i);

				Integer scorePlayer1 = 0;
				Integer scorePlayer2 = 0;

				//active coach boost for player if coach random position is equal with player's position(home)
				if (teamOneCoach.getBoostedPosition().equals(playerHome.getPosition())) {
					scorePlayer1 += teamOneCoach.getSpecialValueForPosition();

					StringBuilder coachHome = new StringBuilder();
					coachHome
							.append(teamOneCoach.getName())
							.append("     boosted position:     ")
							.append(teamOneCoach.getBoostedPosition())
							.append("     booster:     ")
							.append(teamOneCoach.getSpecialValueForPosition());

					modelMap.addAttribute("coachHome" + matchCounter, coachHome);
				}
				//active coach boost for player if coach random position is equal with player's position(away)
				if (teamTwoCoach.getBoostedPosition().equals(playerAway.getPosition())) {
					scorePlayer2 += teamTwoCoach.getSpecialValueForPosition();

					StringBuilder coachAway = new StringBuilder();
					coachAway
							.append(teamTwoCoach.getName())
							.append("     boosted position:     ")
							.append(teamTwoCoach.getBoostedPosition())
							.append("     booster:     ")
							.append(teamTwoCoach.getSpecialValueForPosition());

					modelMap.addAttribute("coachAway" + matchCounter, coachAway);
				}

				scorePlayer1 = (scorePlayer1 + playerHome.getOverall()) * 6;
				scorePlayer2 = (scorePlayer2 + playerAway.getOverall()) * 6;

				//active crowd factor if score difference is higher than 6
				if (Math.abs(scorePlayer1 - scorePlayer2) > 6) {
					if (isHomeBoost) {
						scorePlayer1 += crowdFactor;
					} else {
						scorePlayer2 += crowdFactor;
					}
				}

				StringBuilder battle = new StringBuilder();
				battle
						.append("(")
						.append(playerHome.getPosition())
						.append(")")
						.append("[")
						.append(playerHome.getOverall())
						.append("]")
						.append(playerHome.getName())
						.append("     vs     ")
						.append("(")
						.append(playerAway.getPosition())
						.append(")")
						.append("[")
						.append(playerAway.getOverall())
						.append("]")
						.append(playerAway.getName())
						.append("     score     ")
						.append(scorePlayer1)
						.append("     :     ")
						.append(scorePlayer2);

				teamOneScore += scorePlayer1;
				teamTwoScore += scorePlayer2;

				StringBuilder summary = new StringBuilder();
				summary
						.append("     summary     ")
						.append(teamOneScore)
						.append("     :     ")
						.append(teamTwoScore);

				//active if score is equal after all 5 battles
				if (i == 4 && teamOneScore.equals(teamTwoScore)) {
					if (teamOneCoach.getSpecialValueForPosition() > teamTwoCoach.getSpecialValueForPosition()) {
						teamOneScore++;
					} else {
						if (isOvertimeCrowdFactorHome) {
							teamOneScore += overtimeBooster;
						} else {
							teamTwoScore += overtimeBooster;
						}
					}

					summary
							.append("     overtime!!!     ")
							.append(teamOneScore)
							.append("     :     ")
							.append(teamTwoScore);
				}

				modelMap.addAttribute("battle" + (i + 1) + matchCounter, battle);
				modelMap.addAttribute("summary" + (i + 1) + matchCounter, summary);

				//set flag statusAfterLastGame after battle. Flag used in setNextRoundMatches
				if (scorePlayer1 > scorePlayer2) {
					playerHome.setStatusAfterLastGame("won");
					playerAway.setStatusAfterLastGame("lost");
				} else {
					playerHome.setStatusAfterLastGame("lost");
					playerAway.setStatusAfterLastGame("won");
				}
			}

			matchCounter++;

			if (!isPageReload) {
				//set winner
				if (teamOneScore > teamTwoScore) {
					team1.setWin();
				} else {
					team2.setWin();
				}

				//set plus minus balance and increase games played
				team1.setPlusMinus(teamOneScore - teamTwoScore);
				team1.setSeasonGamesPlayed();
				team2.setPlusMinus(teamTwoScore - teamOneScore);
				team2.setSeasonGamesPlayed();

				//update teams
				saveTeam(team1);
				saveTeam(team2);

				//save match
				match.setTeamOneScore(teamOneScore);
				match.setTeamTwoScore(teamTwoScore);
				saveMatch(match);
			}
		}

		isPageReload = true;
	}

	public void finishRoundInit(HttpServletResponse response) throws IOException {

		log.warn("finishRoundInit");

		isPageReload = false;

		boolean isSeasonEnd = getMaxLeftOpponentsFromAllTeams(owner) == 0;

		if (isSeasonEnd) {
			response.sendRedirect("/game/awards");

		} else {
			setNextRoundMatches();
			response.sendRedirect("/game/team");
		}

	}

	public void getTablePageInit(ModelMap map) {

		teams.sort(HandleComparator.compareForSorting());

		map.addAttribute("teams", teams);
	}

	public void getAwardsPageInit(ModelMap map) {

		teams.sort(HandleComparator.compareForSorting());
		map.addAttribute("teams", teams);

		Player mvp = getMvp(owner);
		map.addAttribute("mvp", mvp);

		String firstName = mvp.getName().split(" ")[0];
		String lastName = mvp.getName().split(" ")[1];
		map.addAttribute("firstName", firstName);
		map.addAttribute("lastName", lastName);

		StringBuilder companyPresidiumComment = new StringBuilder();
		for (int i = 0; i < teams.size(); i++) {
			if (teams.get(i).getName().equals(userTeam.getName())) {
				switch (i) {
					case 0:
						companyPresidiumComment.append("RING! is yours! You are a true legend! Ring is forever and fame will never die! ");
						//create ring
						addRing(owner, userTeam, season);
						break;
					case 1:
						companyPresidiumComment.append("SILVER! Congrats! You are skilled enough to become a legend!");
						break;
					case 2:
						companyPresidiumComment.append("BRONZE! This is something! We appreciate your effort and wish at least the same position next year!");
						break;
					case 3:
						companyPresidiumComment.append("You were so close to get the bronze! We don't know what to think about that..");
						break;
					case 4:
						companyPresidiumComment.append("Do you know something about basketball?! Terrible!");
						break;
					case 5:
						companyPresidiumComment.append("You're a one of the worst coach I've ever seen!!");
						break;
					default:
						companyPresidiumComment.append("Your final position makes me sick!!!");
						break;
				}
			}
			map.addAttribute("comment", companyPresidiumComment);
			map.addAttribute("winner", teams.get(0).getName());
			map.addAttribute("second", teams.get(1).getName());
			map.addAttribute("third", teams.get(2).getName());
		}
	}

	public void finishSeasonInit() {

		setNextRoundMatches();
	}

	public void getRingsPageInit(ModelMap map) {

		List<Ring> ringsByOwner = getRingsByOwner(owner);

		boolean isAnyRing = ringsByOwner.size() > 0;

		StringBuilder ringPath = new StringBuilder();
		ringPath
				.append(userTeam.getName().substring(0,3))
				.append("/ring.jpeg");

		map.addAttribute("ringPath", ringPath);
		map.addAttribute("isAnyRing", isAnyRing);
		map.addAttribute("rings", ringsByOwner);
	}

	public void getTradePageInit(ModelMap map) {

		List<Player> myPlayers = getPlayersByTeam(userTeam);
		List<Player> availablePlayersToTrade = new ArrayList<>();
		String nextOpponent = null;

		for (Match nextRoundMatch : nextRoundMatches) {
			if (nextRoundMatch.getTeamOne().getName().equals(userTeam.getName())) {
				nextOpponent = nextRoundMatch.getTeamTwo().getName();
				break;
			} else if (nextRoundMatch.getTeamTwo().getName().equals(userTeam.getName())) {
				nextOpponent = nextRoundMatch.getTeamOne().getName();
				break;
			}
		}

		String finalNextOpponent = nextOpponent;
		teams.forEach(t -> {
			if (!t.getName().equals(userTeam.getName()) && !t.getName().equals(finalNextOpponent)) {
				List<Player> playersByTeam = getPlayersByTeam(t);
				availablePlayersToTrade.addAll(playersByTeam);
			}
		});

		map.addAttribute("myPlayers", myPlayers);
		map.addAttribute("availablePlayersToTrade", availablePlayersToTrade);
	}

	public void tradeMakerInit(Integer playerId) {

		Player wantedPlayer = getPlayerById(playerId).get();

		Player myPlayer = getPlayersByTeam(userTeam)
				.stream().filter(p -> p.getPosition() == wantedPlayer.getPosition())
				.iterator().next();

		if (isTradeDone(wantedPlayer, myPlayer)) {
			tradeInfo = "Trade is done. Good job!";
		} else {
			tradeInfo = "Teams did not come to an agreement. You have last chance to trade before next game.";
		}

		tradesCounter++;
	}

	public void getTop10Init(ModelMap map) {

		List<Object[]> top10data = getTop10Data();

		map.addAttribute("top10data", top10data);
	}


	//helpers for section above

	/**
	 * trade mechanics
	 * @param wantedPlayer
	 * @param myPlayer
	 * @return
	 */
	private boolean isTradeDone(Player wantedPlayer, Player myPlayer) {

		int overallDifference = wantedPlayer.getOverall() - myPlayer.getOverall();
		int contractsDifference = wantedPlayer.getContractValue() - myPlayer.getContractValue();

		if (Math.abs(contractsDifference) > 3) {
			return false;
		} else {
			Team opponentTeam = wantedPlayer.getTeam();

			//If you obtain player with higher overall and lower contract
			if (overallDifference > 0 && contractsDifference < 0) {
				boolean isDone = new Random().nextBoolean();
				if (isDone) {
					return makeExchange(wantedPlayer, myPlayer, opponentTeam);
				} else {
					return false;
				}
				//If you obtain player with lower overall and higher contract
			} else if (overallDifference < 0 && contractsDifference > 0) {
				boolean isUserNewPlayerBoosted = Arrays.asList(false, false, false, false, false, true, false, false, false, false)
						.get(new Random().nextInt(10));
				if (isUserNewPlayerBoosted) {
					wantedPlayer.setOverall(wantedPlayer.getOverall() + new Random().nextInt(contractsDifference) + 1);
				}
			}
			return makeExchange(wantedPlayer, myPlayer, opponentTeam);
		}
	}

	/**
	 * set new team for traded players and update players
	 */
	private boolean makeExchange(Player wantedPlayer, Player myPlayer, Team opponentTeam) {

		wantedPlayer.setTeam(userTeam);
		myPlayer.setTeam(opponentTeam);

		savePlayer(wantedPlayer);
		savePlayer(myPlayer);
		return true;
	}

	/**
	 * handle httpSession
	 * each guest has its own sessionId
	 *
	 * @param request
	 * @return
	 */
	private Owner getOwner(HttpServletRequest request) {

		HttpSession session = request.getSession();
		String id = session.getId();

		return userService.getOwnerBySessionId(id);
	}

	/**
	 * select team which is controlled by user
	 */
	private void setTeamForOldUser() {

		getTeamsByOwner(owner);

		teams.forEach(team -> {
			if (team.isControlledByPlayer()) {
				userTeam = team;
			}
		});
	}

	/**
	 * set user's team based on given team name
	 * @param team
	 */
	private void setTeamForNewUser(String team) {

		teams.forEach(t -> {
			if (t.getName().equals(team)) {
				t.setControlledByPlayer(true);
				userTeam = t;
			}
		});
	}

	/**
	 * reset:
	 * -usedTeams list
	 * -nextRoundMatches list
	 * -opponents map with distinct team as a key and the rest available teams as opponents
	 * update opponents map with available teams based on isNewSeason
	 */
	private void setNextRoundMatches() {

		tradeInfo = null;
		tradesCounter = 0;

		nextRoundMatches = new LinkedList<>();

		List<Team> usedTeams = new LinkedList<>();

		Map<Team, List<Team>> opponents = new HashMap<>();

		boolean isNewSeason = getMaxLeftOpponentsFromAllTeams(owner) == 0;

		if (isNewSeason) {

			season++;

			teams.forEach(team -> opponents.put(team, new LinkedList<>()));
			for (Map.Entry<Team, List<Team>> entry : opponents.entrySet()) {
				teams.forEach(team -> {

					team.resetForNewSeason();

					getPlayersByTeam(team).forEach(Player::resetForNewSeason);

					if (team != entry.getKey()) {
						entry.getValue().add(team);
					}
				});
			}
		} else {

			getTeamsByOwner(owner);

			teams.forEach(team -> {

				opponents.put(team, team.getLeftOpponents());

				getPlayersByTeam(team).forEach(player -> {
					player.updateValuesAfterLastGame();
					player.setStatusAfterLastGame("noGame");
				});
			});
		}

		createMatchesForNextRound(usedTeams, opponents);
	}

	/**
	 * create matches based on usedTeams list and opponents map
	 * add used teams to usedTeams list
	 * set leftOpponents field for each used team
	 * update team in database
	 *
	 * @param usedTeams
	 * @param opponents
	 */
	private void createMatchesForNextRound(List<Team> usedTeams, Map<Team, List<Team>> opponents) {

		//check last planned match (not yet played)
		for (Map.Entry<Team, List<Team>> entry : opponents.entrySet()) {

			if (entry.getKey().getLastMatch() != null && entry.getKey().getLastMatch().getTeamOneScore() == 0) {

				log.warn("founded expecting games after log in ");

				nextRoundMatches.add(entry.getKey().getLastMatch());

				usedTeams.add(entry.getKey().getLastMatch().getTeamOne());
				usedTeams.add(entry.getKey().getLastMatch().getTeamTwo());
			}
		}


		for (Map.Entry<Team, List<Team>> entry : opponents.entrySet()) {

			log.warn(String.valueOf(entry.getKey().getLeftOpponents().size()));

			List<Player> players = getPlayersByTeam(entry.getKey());
			players.forEach(Player::setRandomOverall);

			Coach coach = getCoachByTeam(entry.getKey());
			coach.setRandomPosition();
			coach.setRandomValueForPosition();

			if (usedTeams.contains(entry.getKey())) {

				entry.getKey().setLeftOpponents(entry.getValue());

				saveTeam(entry.getKey());

				continue;
			}

			for (Team t : entry.getValue()) {

				if (!usedTeams.contains(t)) {

					Match match = new Match(new Date(), entry.getKey(), t);
					saveMatch(match);

					nextRoundMatches.add(match);

					usedTeams.add(entry.getKey());
					usedTeams.add(t);

					entry.getKey().setLastMatch(match);

					entry.getValue().remove(t);
					entry.getKey().setLeftOpponents(entry.getValue());

					saveTeam(entry.getKey());

					break;
				}
			}
		}
	}


}
