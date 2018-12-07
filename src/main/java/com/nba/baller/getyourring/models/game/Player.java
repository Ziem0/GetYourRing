package com.nba.baller.getyourring.models.game;


import com.nba.baller.getyourring.helpers.Position;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.*;
import java.util.Random;

@Slf4j
@Entity
@Getter
@Setter
public class Player {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(nullable = false)
	private String name;

	@Enumerated(EnumType.STRING)
	private Position position;

	@OneToOne
	private Team team;

	private Integer overall;

	private Integer contractValue;

	private Integer wonBattlesPerSeason;

	private Integer wonBattlesEntireCareer;

	private String statusAfterLastGame;

	public Player() {
	}

	public Player(String name, Position position, Team team, Integer overall, Integer contractValue, Integer wonBattlesPerSeason, Integer wonBattlesEntireCareer, String statusAfterLastGame) {
		this.name = name;
		this.position = position;
		this.team = team;
		this.overall = overall;
		this.contractValue = contractValue;
		this.wonBattlesPerSeason = wonBattlesPerSeason;
		this.wonBattlesEntireCareer = wonBattlesEntireCareer;
		this.statusAfterLastGame = statusAfterLastGame;
	}

	public Player(String name, Position position, Team team) {
		this.name = name;
		this.position = position;
		this.team = team;
		this.overall = new Random().nextInt(6-1)+1;
		this.contractValue = new Random().nextInt(11 - 3) + 3;
		this.wonBattlesPerSeason = 0;
		this.wonBattlesEntireCareer = 0;
		this.statusAfterLastGame = "noGame";
	}

	public void setRandomOverall() {
		this.overall = new Random().nextInt(6-1)+1;
	}

	public void setRandomContractValue() {
		this.contractValue = new Random().nextInt(11 - 3)+3;
	}

	public void resetForNewSeason() {
		setRandomContractValue();
		this.wonBattlesPerSeason = 0;
		setRandomOverall();
	}

	private void setNewValuesAfterWonGame() {
		setRandomOverall();
		this.wonBattlesPerSeason++;
		this.wonBattlesEntireCareer++;
	}

	private void setNewValuesAfterLostGame() {
		setRandomOverall();
		this.wonBattlesPerSeason--;
		this.wonBattlesEntireCareer--;
	}

	public void updateValuesAfterLastGame() {
		if (statusAfterLastGame.equals("won")) {
			setNewValuesAfterWonGame();
		} else if (statusAfterLastGame.equals("lost")) {
			setNewValuesAfterLostGame();
		}
	}

}
