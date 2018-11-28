package com.nba.baller.getyourring.models.game;


import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Random;

@Slf4j
@Entity
@Getter
@Setter
public class Coach {

	@Id
	@Column(nullable = false)
	private String name;

	private Integer specialValueForPosition;

	public Coach() {
	}

	public Coach(String name) {
		this.name = name;
		this.specialValueForPosition = getRandom() ? -1 : 1;
	}

	private boolean getRandom() {
		return new Random().nextBoolean();
	}




}
