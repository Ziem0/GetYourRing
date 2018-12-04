package com.nba.baller.getyourring.controllers;

import com.nba.baller.getyourring.helpers.Position;
import com.nba.baller.getyourring.models.Owner;
import com.nba.baller.getyourring.models.game.Coach;
import com.nba.baller.getyourring.models.game.Match;
import com.nba.baller.getyourring.models.game.Player;
import com.nba.baller.getyourring.models.game.Team;
import com.nba.baller.getyourring.services.GameService;
import com.nba.baller.getyourring.services.UserService;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.*;

@Slf4j
@Setter
@Controller
@RequestMapping("")
public class GameController {

	private Owner owner;

	private boolean isOwnerNew;

	private Team userTeam;

	private List<Team> teams;

	private List<Match> nextRoundMatches;

	final
	UserService userService;

	final
	GameService gameService;

	@Autowired
	public GameController(UserService userService, GameService gameService) {
		this.userService = userService;
		this.gameService = gameService;
	}


	/**
	 * responsible for:
	 * 	 -setting class fields
	 * 	 -create new content if new user is present
	 * 	 -else load previous content
	 * 	 -redirect to intro page
	 * @param response
	 * @param request
	 * @throws IOException
	 */
	@GetMapping("/game")
	public void game(HttpServletResponse response,
	                 HttpServletRequest request) throws IOException {

		isOwnerNew = false;
		setOwner(getOwner(request));
		setTeams(gameService.getTeamsByOwner(owner));

		if (!teams.iterator().hasNext()) {
			gameService.addNewGameContent(owner);
			setTeams(gameService.getTeamsByOwner(owner));
			isOwnerNew = true;

		} else {
			setTeamForOldUser();
		}

		response.sendRedirect("/game/intro");
	}

	/**
	 * game rules
	 * player's team selection when new user is present
	 * @param model
	 * @return intro.html
	 */
	@GetMapping("/game/intro")
	public String chooseTeam(Model model) {
		List<String> teamsNames = new ArrayList<>();
		teams.forEach(team -> teamsNames.add(team.getName()));
		model.addAttribute("isOwnerNew", isOwnerNew);
		model.addAttribute("teams", teamsNames);

		return "intro";
	}

	/**
	 * responsible for:
	 *  -set user's team and update database
	 *  -create first round matches
	 * automatically redirect to team page
	 * @param team
	 * @param response
	 * @throws IOException
	 */
	@PostMapping("/game/intro")
	public void getUserTeam(String team, HttpServletResponse response) throws IOException {
		setTeamForNewUser(team);

		gameService.updateUserTeam(userTeam);

		setNextRoundMatches();

		response.sendRedirect("/game/team");
	}

	/**
	 * control center for game app
	 * view my:
	 *  -players
	 *  -coach
	 *  -city
	 *  -hall
	 *  -plus minus
	 *  -next round matches
	 * @param map [players, coach, city, hall, wins, balance]
	 * @return team.html
	 */
	@GetMapping("/game/team")
	public String getTeamPage(ModelMap map) {
		List<Player> players = gameService.getPlayersByTeam(userTeam);
		Coach coach = gameService.getCoachByTeam(userTeam);

		map.addAttribute("players", players);
		map.addAttribute("coach", coach);
		map.addAttribute("city", userTeam.getCity().getName());
		map.addAttribute("hall", userTeam.getHall().getName());
		map.addAttribute("wins", userTeam.getWins());
		map.addAttribute("balance", userTeam.getPlusMinus());
		map.addAttribute("matches", nextRoundMatches);

		return "team";
	}

	/**
	 * reload:
	 * -coach skills
	 * -all players overall
	 *
	 * @return matches.html
	 */
	@GetMapping("/game/matches")
	public String getMatchesPage(ModelMap modelMap) {
		/*
		match by match
		match divided into 5 parts
		parts got players sorted by position
		need:
		teamOne hall for background
		teamOneScore teamTwoScore
		players overall and position
		coach boost - special value for position and position
		 */

		modelMap.addAttribute("one",1);
		modelMap.addAttribute("two", 2);

		modelMap.addAttribute("nextRoundMatches", nextRoundMatches);

		int count = 0;

		for (Match match : nextRoundMatches) {

			Team team1 = match.getTeamOne();
			Team team2 = match.getTeamTwo();

			Integer teamOneScore = match.getTeamOneScore();
			Integer teamTwoScore = match.getTeamTwoScore();

			Coach teamOneCoach = team1.getCoach();
			Coach teamTwoCoach = team2.getCoach();

			Position oneCoachRandomPosition = teamOneCoach.getRandomPosition();
			Position twoCoachRandomPosition = teamTwoCoach.getRandomPosition();

			String city = team1.getCity().getName();
			String hall = team1.getHall().getName();
			modelMap.addAttribute("city"+count, city);
			modelMap.addAttribute("hall"+count, hall);

			List<Player> playersByTeam1 = gameService.getPlayersByTeam(team1);
			List<Player> playersByTeam2 = gameService.getPlayersByTeam(team2);

			for (int i = 0; i < playersByTeam1.size(); i++) {

				Player playerHome = playersByTeam1.get(i);
				Player playerAway = playersByTeam2.get(i);

				Integer scorePlayer1 = playerHome.getOverall() * 6;
				Integer scorePlayer2 = playerAway.getOverall() * 6;

				if (oneCoachRandomPosition == playerHome.getPosition()) {
					playerHome.setOverall(playerHome.getOverall() + teamOneCoach.getSpecialValueForPosition());
				}
				if (twoCoachRandomPosition == playerAway.getPosition()) {
					playerAway.setOverall(playerAway.getOverall() + teamTwoCoach.getSpecialValueForPosition());
				}

				if (Math.abs(scorePlayer1 - scorePlayer2) > 6) {
					boolean isHomeBoost = new Random().nextBoolean();
					int crowdFactor = new Random().nextInt(13 - 6) + 1;
					if (isHomeBoost) {
						scorePlayer1 += crowdFactor;
					} else {
						scorePlayer2 += crowdFactor;
					}
				}

				StringBuilder battle = new StringBuilder();
				battle
						.append(playerHome.getName())
						.append("     vs     ")
						.append(playerAway.getName())
						.append("     score     ")
						.append(scorePlayer1)
						.append("     :     ")
						.append(scorePlayer2)
						.append("\n");

				teamOneScore += scorePlayer1;
				teamTwoScore += scorePlayer2;

				StringBuilder summary = new StringBuilder();
				summary
						.append("summary")
						.append(teamOneScore)
						.append("     :     ")
						.append(teamTwoScore)
						.append("\n");

				if (i == 4 && teamOneScore.equals(teamTwoScore)) {
				}

//				modelMap.addAttribute(String.valueOf(i+count+10), battle);
//				modelMap.addAttribute(String.valueOf(i+count+20), summary);
				modelMap.addAttribute("battle" + (i + 1) + count, battle);
				modelMap.addAttribute("summary" + (i + 1) + count, summary);

			}

			count++;

			//save zone
			match.setTeamOneScore(teamOneScore);
			match.setTeamTwoScore(teamTwoScore);
			gameService.saveMatch(match);

			if (teamOneScore > teamTwoScore) {
				team1.setWin();
				team1.setPlusMinus(teamOneScore-teamTwoScore);
			} else {
				team2.setWin();
				team2.setPlusMinus(teamTwoScore-teamOneScore);
			}
			gameService.saveTeam(team1);
			gameService.saveTeam(team2);
		}

		return "matches";
	}

	/**
	 *
	 * set next round matches
	 * redirect to /game/team
	 */
	@PostMapping("/game/matches")
	public void finishRound() {
		setNextRoundMatches();
	}


	@GetMapping("/game/trade")
	public String getTradePage() {
		return "trade";
	}

	@GetMapping("/game/table")
	public String getTablePage() {
		return "table";
	}

	@GetMapping("/game/awards")
	public String getAwardsPage() {
		/*
		Reload:
		-players contracts
		-add ring
		 */
		return "awards";
	}

//	@GetMapping("/game/player")
//	public String getPlayerPage() {
//		return "player";
//	}

//	@GetMapping("/game/coach")
//	public String getCoachPage() {
//		return "coach";
//	}



	//HELPERS
	//if session expired, return to login page
	//HttpSession only for Owner's purposes; not check if session expired
	//responsible for that is SpringSecurity

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
		teams.forEach(team -> {
			if (team.isControlledByPlayer()) {
				userTeam = team;
			}
		});
	}

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
	 *  -usedTeams list
	 *  -nextRoundMatches list
	 *  -opponents map with distinct team as a key and the rest available teams as opponents
	 * update opponents map with available teams based on isNewSeason
	 */
	private void setNextRoundMatches() {

		nextRoundMatches = new LinkedList<>();

		List<Team> usedTeams = new LinkedList<>();

		Map<Team, List<Team>> opponents = new HashMap<>();

		boolean isNewSeason = gameService.getMaxLeftOpponentsFromAllTeams() == 0;

		if (isNewSeason) {
			teams.forEach(team -> opponents.put(team, new LinkedList<>()));
			for (Map.Entry<Team, List<Team>> entry : opponents.entrySet()) {
				teams.forEach(team -> {
					if (team != entry.getKey()) {
						entry.getValue().add(team);
					}
				});
			}
		} else {
			teams.forEach(team -> opponents.put(team, team.getLeftOpponents()));
		}

		createMatchesForNextRound(usedTeams, opponents);
	}

	/**
	 * create matches based on usedTeams list and opponents map
	 * add used teams to usedTeams list
	 * set leftOpponents field for each used team
	 * update team in database
	 * @param usedTeams
	 * @param opponents
	 */
	private void createMatchesForNextRound(List<Team> usedTeams, Map<Team, List<Team>> opponents) {
		for (Map.Entry<Team, List<Team>> entry : opponents.entrySet()) {

			if (usedTeams.contains(entry.getKey())) {
				entry.getKey().setLeftOpponents(entry.getValue());
				gameService.saveTeam(entry.getKey());
				continue;
			}

			for (Team t : entry.getValue()) {
				if (!usedTeams.contains(t)) {
					Match match = new Match(new Date(), entry.getKey(), t);
					nextRoundMatches.add(match);
					usedTeams.add(entry.getKey());
					usedTeams.add(t);
					entry.getValue().remove(t);
					entry.getKey().setLeftOpponents(entry.getValue());
					gameService.saveTeam(entry.getKey());
					break;
				}
			}
		}
	}

}

