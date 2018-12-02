package com.nba.baller.getyourring.controllers;

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
	 * responsible for set user's team and update database
	 * automatically redirect to team page
	 * @param team
	 * @param response
	 * @throws IOException
	 */
	@PostMapping("/game/intro")
	public void getUserTeam(String team, HttpServletResponse response) throws IOException {
		setTeamForNewUser(team);

		gameService.updateUserTeam(userTeam);

		response.sendRedirect("/game/team");
	}

	/**
	 * control center for game app
	 * @param map [players, coach, city, hall, wins, balance]
	 * @return team.html
	 */
	@GetMapping("/game/team")
	public String getTeamPage(ModelMap map) {
		List<Player> players = gameService.getPlayersByTeam(userTeam);
		Coach coach = gameService.getCoachByTeam(userTeam);
		setNextRoundMatches();

		map.addAttribute("players", players);
		map.addAttribute("coach", coach);
		map.addAttribute("city", userTeam.getCity().getName());
		map.addAttribute("hall", userTeam.getHall().getName());
		map.addAttribute("wins", userTeam.getWins());
		map.addAttribute("balance", userTeam.getPlusMinus());

		System.out.println("nextRound");
		nextRoundMatches.forEach(match -> System.out.println(match.getTeamOne().getName() + "--" + match.getTeamTwo().getName() + "\n"));


		return "team";
	}

	@GetMapping("/game/matches")
	public String getMatchesPage() {
		/*
		Reload:
		-coach skills
		-all players overall
		-next opponent
		 */

		return"matches";
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
					Match match = new Match(new Date(), entry.getKey(), t, 0, 0);
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

