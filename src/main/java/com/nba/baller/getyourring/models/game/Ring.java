package com.nba.baller.getyourring.models.game;


import com.nba.baller.getyourring.models.Owner;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.*;
import java.util.Date;

@Slf4j
@Entity
@Getter
@Setter
public class Ring {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(nullable = false)
	@Temporal(TemporalType.DATE)
	private Date date;

	@ManyToOne
	private Owner owner;

	@Column(nullable = false)
	private String team;

	@Column(nullable = false)
	private Integer season;

	public Ring() {
	}

	public Ring(Owner owner, String team, Integer season) {
		this.date = new Date();
		this.owner = owner;
		this.team = team;
		this.season = season;
	}

	public Ring(Date date, Owner owner, String team, Integer season) {
		this.date = date;
		this.owner = owner;
		this.team = team;
		this.season = season;
	}
}

