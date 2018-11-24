package com.nba.baller.getyourring.models;

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
	private Owner user;

	private boolean hasOwner;

	private Integer wins;

	private Integer plusMinus;


	public Team() {
	}

	public Team(String name, Hall hall, City city, Coach coach, Owner user, Integer wins, Integer plusMinus) {
		this.name = name;
		this.hall = hall;
		this.city = city;
		this.coach = coach;
		this.user = user;
		this.hasOwner = user != null;
		this.wins = wins;
		this.plusMinus = plusMinus;
	}
}
