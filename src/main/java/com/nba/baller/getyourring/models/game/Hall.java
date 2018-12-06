package com.nba.baller.getyourring.models.game;


import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.*;
import java.util.Arrays;
import java.util.Random;

@Slf4j
@Entity
@Setter
@Getter
public class Hall {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(nullable = false)
	private String name;

	@Column(nullable = false)
	private int crowdFactor;

	@Column(nullable = false)
	private boolean isCrowdFactorHome;

	@Column(nullable = false)
	private boolean isOvertimeCrowdFactorHome;

	@Column(nullable = false)
	private int overtimeScoreBooster;


	public Hall() {
	}

	public Hall(String name) {
		this.name = name;
		this.crowdFactor = setRandomCrowdFactor();
		this.isCrowdFactorHome = setIsCrowdFactorHome();
		this.isOvertimeCrowdFactorHome = setIsCrowdFactorHome();
		this.overtimeScoreBooster = setRandomCrowdFactor();
	}

	public int setRandomCrowdFactor() {
		return this.crowdFactor = new Random().nextInt(13 - 6) + 1;
	}

	public boolean setIsCrowdFactorHome() {
		boolean isHomeBoost = Arrays.asList(true, false, true, false, true).get(new Random().nextInt(5));
		return isHomeBoost;
	}
}
