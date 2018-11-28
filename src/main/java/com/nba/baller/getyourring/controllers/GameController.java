package com.nba.baller.getyourring.controllers;

import com.nba.baller.getyourring.models.GameSession;
import com.nba.baller.getyourring.models.Owner;
import com.nba.baller.getyourring.models.game.Team;
import com.nba.baller.getyourring.services.GameService;
import com.nba.baller.getyourring.services.UserService;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@Slf4j
@Setter
@Controller
@RequestMapping("")
public class GameController {

	private Owner owner;
	private Iterable<Team> teams;

	final
	UserService userService;

	final
	GameService gameService;

	@Autowired
	public GameController(UserService userService, GameService gameService) {
		this.userService = userService;
		this.gameService = gameService;
	}

	@GetMapping("/game")
	public String game(HttpServletResponse response,
	                   HttpServletRequest request, Model model) throws IOException {

		setOwner(getOwner(request));
		setTeams(gameService.getTeamsByOwner(owner));

		if (!teams.iterator().hasNext()) {
			gameService.addNewGameContent(owner);
			setTeams(gameService.getTeamsByOwner(owner));

			log.warn("new content was added");
			model.addAttribute("message", "empty");

			return chooseTeam();
		}

		return "game";
	}

	private String chooseTeam() {
		//choose my team based on teams list
		// where intro?
		return "demo";
	}


	//if session expired, return to login page
	//HttpSessiononly for Owner's purposes; not check if session expired
	//responsible for that is SpringSecurity
	private Owner getOwner(HttpServletRequest request) {
		HttpSession session = request.getSession();
		String id = session.getId();
		GameSession gameSession = userService.getSessionBySessionId(id);

		return userService.getOwnerBySessionId(id);
	}

}
