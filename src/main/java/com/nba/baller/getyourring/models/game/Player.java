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
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	@Column(nullable = false)
	private String name;

	@Enumerated(EnumType.STRING)
	private Position position;

	@OneToOne
	private Team team;

	private Integer overall;

	private Integer contractValue;


	public Player() {
	}

	public Player(String name, Position position, Team team) {
		this.name = name;
		this.position = position;
		this.team = team;
		this.overall = new Random().nextInt(6-1)+1;
		this.contractValue = new Random().nextInt(11 - 3)+3;
	}

	public void setOverall(Integer overall) {
		this.overall = new Random().nextInt(6-1)+1;
	}

	public void setContractValue(Integer contractValue) {
		this.contractValue = new Random().nextInt(11 - 3)+3;
	}

}
