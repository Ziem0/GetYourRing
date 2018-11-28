package com.nba.baller.getyourring.models.game;

import com.nba.baller.getyourring.models.Owner;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.*;

@Slf4j
@Entity
@Getter
@Setter
public class Team {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	@Column(nullable = false)
	private String name;

	@OneToOne
	private Hall hall;

	@OneToOne
	private City city;

	@OneToOne
	private Coach coach;

	@OneToOne
	private Owner owner;

	private boolean controlledByPlayer;

	private Integer wins;

	private Integer plusMinus;


	public Team() {
	}

	public Team(String name, Hall hall, City city, Coach coach, Owner owner, Boolean controlledByPlayer) {
		this.name = name;
		this.hall = hall;
		this.city = city;
		this.coach = coach;
		this.owner = owner;
		this.controlledByPlayer = controlledByPlayer;
		this.wins = 0;
		this.plusMinus = 0;
	}

	public Team(String name, Hall hall, City city, Coach coach, Owner owner, Boolean controlledByPlayer, Integer wins, Integer plusMinus) {
		this.name = name;
		this.hall = hall;
		this.city = city;
		this.coach = coach;
		this.owner = owner;
		this.controlledByPlayer = controlledByPlayer;
		this.wins = wins;
		this.plusMinus = plusMinus;
	}
}
