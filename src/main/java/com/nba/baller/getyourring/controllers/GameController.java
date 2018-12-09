package com.nba.baller.getyourring.controllers;

import com.nba.baller.getyourring.services.GameService;
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
import java.io.IOException;

@Slf4j
@Controller
@RequestMapping("/")
public class GameController {

	final
	GameService gameService;

	@Autowired
	public GameController(GameService gameService) {
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
	                 HttpServletRequest request) throws IOException {

		gameService.initGameHandler(request);

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

		model.addAttribute("title", "Intro");

		gameService.chooseTeamInit(model);

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
	public void getUserTeam(String team, HttpServletResponse response, HttpServletRequest request) throws IOException {

		gameService.getUserTeamInit(team);

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

		map.addAttribute("title", "MyTeam");

		gameService.getTeamPageInit(map);

		return "team";
	}

	@PostMapping("/game/team")
	public String setNextRound() {

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

		gameService.getMatchesInit(modelMap);

		return "matches";
	}

	/**
	 * set next round matches
	 * redirect to /game/team
	 */
	@PostMapping("/game/matches")
	public void finishRound(HttpServletResponse response, HttpServletRequest request) throws IOException {

		gameService.finishRoundInit(response);
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

		map.addAttribute("title", "Table");

		gameService.getTablePageInit(map);

		return "table";
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

		map.addAttribute("title", "Awards");

		gameService.getAwardsPageInit(map);

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
	public void finishSeason(HttpServletResponse response) throws IOException {

		gameService.finishSeasonInit();


		response.sendRedirect("/game/team");
	}

	/**
	 * load all rings by user
	 * @param map
	 * @return
	 */
	@GetMapping("/game/rings")
	public String getRingsPage(ModelMap map) {

		map.addAttribute("title", "MyRings");

		gameService.getRingsPageInit(map);

		return "rings";
	}

	/**
	 * check next opponent
	 * set my current players and available players(without players which belongs to next opponent)
	 * pass myPlayers and availablePlayers to trade.html
	 * @param map
	 * @return
	 */
	@GetMapping("/game/trade")
	public String getTradePage(ModelMap map) {

		map.addAttribute("title", "Trades");

		gameService.getTradePageInit(map);

		return "trade";
	}

	/**
	 * create players to trade based on given playerId
	 * init trade mechanic
	 * increase tradeCounter
	 * after all redirect to /game/team
	 * @param response
	 * @param playerId
	 * @throws IOException
	 */
	@PostMapping("/game/trade")
	public void tradeMaker(HttpServletResponse response, Integer playerId) throws IOException {

		gameService.tradeMakerInit(playerId);

		response.sendRedirect("/game/team");
	}

	/**
	 * get info about looted rings sorted by username
	 * pass list with data to top10.html
	 * @param map
	 * @return
	 */
	@GetMapping("/game/top10")
	public String getTop10(ModelMap map) {

		map.addAttribute("title", "Top10");

		gameService.getTop10Init(map);

		return "top10";
	}

}

//todo:
//refactor .html
//need static content reader!
//css

//team flag for new round connected with logout

//check when set next round during init game (intro?)