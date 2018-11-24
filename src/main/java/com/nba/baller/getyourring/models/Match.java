package com.nba.baller.getyourring.models;


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
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	@Column(nullable = false)
	@Temporal(TemporalType.DATE)
	private Date date;

	@OneToOne
	@Column(nullable = false)
	private Team teamOne;

	@OneToOne
	@Column(nullable = false)
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
}
