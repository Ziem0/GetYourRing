package com.nba.baller.getyourring.models.game;


import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.*;
import java.util.Date;

@Slf4j
@Entity
@Getter
@Setter
public class Match {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(nullable = false)
	@Temporal(TemporalType.DATE)
	private Date date;

	@OneToOne
	private Team teamOne;

	@OneToOne
	private Team teamTwo;

	@Column(nullable = false)
	private Integer teamOneScore;

	@Column(nullable = false)
	private Integer teamTwoScore;

	public Match() {
	}

	public Match(Date date, Team teamOne, Team teamTwo, Integer teamOneScore, Integer teamTwoScore) {
		this.date = date;
		this.teamOne = teamOne;
		this.teamTwo = teamTwo;
		this.teamOneScore = teamOneScore;
		this.teamTwoScore = teamTwoScore;
	}

	public Match(Date date, Team teamOne, Team teamTwo) {
		this.date = date;
		this.teamOne = teamOne;
		this.teamTwo = teamTwo;
		this.teamOneScore = 0;
		this.teamTwoScore = 0;
	}


}
