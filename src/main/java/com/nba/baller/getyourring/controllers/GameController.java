package com.nba.baller.getyourring.controllers;

import com.nba.baller.getyourring.helpers.TeamsComparator;
import com.nba.baller.getyourring.models.Owner;
import com.nba.baller.getyourring.models.game.*;
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

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.time.LocalDate;
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

	private boolean isPageReload = false;

	private Integer season;


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
	 * -setting class fields
	 * -create new content if new user is present
	 * -else load previous content
	 * -redirect to intro page
	 *
	 * @param response
	 * @param request
	 * @throws IOException
	 */
	@GetMapping("/game")
	public void game(HttpServletResponse response,
	                 HttpServletRequest request) throws IOException, ServletException {

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
	 *
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
	 * -set user's team and update database
	 * -create first round matches
	 * automatically redirect to team page
	 *
	 * @param team
	 * @param response
	 * @throws IOException
	 */
	@PostMapping("/game/intro")
	public void getUserTeam(String team, HttpServletResponse response, HttpServletRequest request) throws IOException, ServletException {
		season = LocalDate.now().getYear()-1;

		setTeamForNewUser(team);

		gameService.updateUserTeam(userTeam);

		setNextRoundMatches();

		isOwnerNew = false;

		response.sendRedirect("/game/team");
	}

	/**
	 * control center for game app
	 * view my:
	 * -players
	 * -coach
	 * -city
	 * -hall
	 * -plus minus
	 * -next round matches
	 *
	 * @param map [players, coach, city, hall, wins, balance]
	 * @return team.html
	 */
	@GetMapping("/game/team")
	public String getTeamPage(ModelMap map) {
		map.addAttribute("season", season);

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
	 * iterate over all matches
	 * create battles
	 * pass needed values to html
	 * set winners
	 * set team balance
	 * update teams
	 *
	 * @return matches.html
	 */
	@GetMapping("/game/matches")
	public String getMatchesPage(ModelMap modelMap) {

		modelMap.addAttribute("nextRoundMatches", nextRoundMatches);

		int matchCounter = 0;

		for (Match match : nextRoundMatches) {

			//reset scoring as a security against hacking(refreshing page)
			match.setTeamOneScore(0);
			match.setTeamTwoScore(0);
			gameService.saveMatch(match);

			Team team1 = match.getTeamOne();
			Team team2 = match.getTeamTwo();
			modelMap.addAttribute("teamHome" + matchCounter, team1.getName());
			modelMap.addAttribute("teamAway" + matchCounter, team2.getName());

			String city = team1.getCity().getName();
			String hall = team1.getHall().getName();
			modelMap.addAttribute("city" + matchCounter, city);
			modelMap.addAttribute("hall" + matchCounter, hall);

			List<Player> playersByTeam1 = gameService.getPlayersByTeam(team1);
			List<Player> playersByTeam2 = gameService.getPlayersByTeam(team2);

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
							.append("     booster value:     ")
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
							.append("     booster value:     ")
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
				gameService.saveTeam(team1);
				gameService.saveTeam(team2);

				//save match
				match.setTeamOneScore(teamOneScore);
				match.setTeamTwoScore(teamTwoScore);
				gameService.saveMatch(match);
			}
		}

		isPageReload = true;
		return "matches";
	}

	/**
	 * set next round matches
	 * redirect to /game/team
	 */
	@PostMapping("/game/matches")
	public void finishRound(HttpServletResponse response, HttpServletRequest request) throws IOException, ServletException {
		isPageReload = false;
		boolean isSeasonEnd = gameService.getMaxLeftOpponentsFromAllTeams(owner) == 0;
		if (isSeasonEnd) {
			response.sendRedirect("/game/awards");
		} else {
			setNextRoundMatches();
			response.sendRedirect("/game/team");
		}
	}

	/**
	 * sort teams list by wins and balance
	 * passes sorted list to table.html
	 *
	 * @param map
	 * @return table.html
	 */
	@GetMapping("/game/table")
	public String getTablePage(ModelMap map) {
		teams.sort(TeamsComparator.compareForSorting());
		map.addAttribute("teams", teams);

		return "table";
	}

	@GetMapping("/game/trade")
	public String getTradePage(ModelMap map) {
		return "trade";
	}

	/**
	 * create ring for user if first place was taken
	 * pass presidium comment
	 * pass podium
	 * pass mini table for teams
	 *
	 * @param map
	 */
	@GetMapping("/game/awards")
	public String getAwardsPage(ModelMap map) {
		//create player generally statistics?
		//create mvp based won battles by player and won games(list position) as a team read from database

		//add new button for special events in game
		//addition display for special coach value and crowd effect and overtime
		//mark player which has been boosted by a coach


		teams.sort(TeamsComparator.compareForSorting());
		map.addAttribute("teams", teams);

		Player mvp = gameService.getMvp(owner);
		map.addAttribute("mvp", mvp);

		StringBuilder companyPresidiumComment = new StringBuilder();
		for (int i = 0; i < teams.size(); i++) {
			if (teams.get(i).getName().equals(userTeam.getName())) {
				switch (i) {
					case 0:
						companyPresidiumComment.append("RING! is yours! You are a true legend! Ring is forever and fame will never die! ");
						//create ring
						gameService.addRing(owner, userTeam, season);
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
		return "awards";
	}

	/**
	 * reload teams with new opponents
	 * update contracts for players
	 * set new round
	 *
	 * @param response
	 * @throws IOException
	 */
	@PostMapping("/game/awards")
	public void finishSeason(HttpServletResponse response, HttpServletRequest request) throws IOException, ServletException {
		setNextRoundMatches();
		response.sendRedirect("/game/team");
	}

//	@GetMapping("/game/player")
//	public String getPlayerPage() {
//		return "player";
//	}

	@GetMapping("/game/rings")
	public String getCoachPage(ModelMap map) {
//		gameService.addRing(owner, userTeam, season);
		List<Ring> ringsByOwner = gameService.getRingsByOwner(owner);
		map.addAttribute("rings", ringsByOwner);

		return "rings";
	}


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
	 * -usedTeams list
	 * -nextRoundMatches list
	 * -opponents map with distinct team as a key and the rest available teams as opponents
	 * update opponents map with available teams based on isNewSeason
	 */
	private void setNextRoundMatches() {

		nextRoundMatches = new LinkedList<>();

		List<Team> usedTeams = new LinkedList<>();

		Map<Team, List<Team>> opponents = new HashMap<>();

		boolean isNewSeason = gameService.getMaxLeftOpponentsFromAllTeams(owner) == 0;

		if (isNewSeason) {

			season++;

			teams.forEach(team -> opponents.put(team, new LinkedList<>()));
			for (Map.Entry<Team, List<Team>> entry : opponents.entrySet()) {
				teams.forEach(team -> {

					team.resetForNewSeason();

					gameService.getPlayersByTeam(team).forEach(Player::resetForNewSeason);

					if (team != entry.getKey()) {
						entry.getValue().add(team);
					}
				});
			}
		} else {

			teams.forEach(team -> {

				opponents.put(team, team.getLeftOpponents());

				gameService.getPlayersByTeam(team).forEach(player -> {
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
		for (Map.Entry<Team, List<Team>> entry : opponents.entrySet()) {

			List<Player> players = gameService.getPlayersByTeam(entry.getKey());
			players.forEach(Player::setRandomOverall);

			Coach coach = gameService.getCoachByTeam(entry.getKey());
			coach.setRandomPosition();
			coach.setRandomValueForPosition();

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

