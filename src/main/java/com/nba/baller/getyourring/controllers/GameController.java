package com.nba.baller.getyourring.controllers;

import com.nba.baller.getyourring.models.GameSession;
import com.nba.baller.getyourring.repositories.SessionRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@Controller
@RequestMapping("")
public class GameController {

	@Autowired
	SessionRepo repo;

	@GetMapping("/game")
//	@ResponseBody
	public String game(HttpServletResponse response, HttpServletRequest request) throws IOException {

		HttpSession session = request.getSession();
		String id = session.getId();
		GameSession gameSession = repo.checkIfLogged(id);


		System.out.println(gameSession.getUser());
//                                                              potrzebne by wyjac ownera!
		if (gameSession == null) {
			return "home";
		} else {
			return "logout";
		}

//		response.sendRedirect("/start"); and "game"
	}


}
