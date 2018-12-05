package com.nba.baller.getyourring.models.game;


import com.nba.baller.getyourring.helpers.Position;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Transient;
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

	@Transient
	private Position boostedPosition;

	public Coach() {
	}

	public Coach(String name) {
		this.name = name;
		this.specialValueForPosition = setRandomValueForPosition();
		this.setRandomPosition();
	}

	private boolean getRandom() {
		return new Random().nextBoolean();
	}

	public void setRandomPosition() {
		Position[] positions = Position.values();
		int i = new Random().nextInt(5);
		boostedPosition = positions[i];
	}

	public int setRandomValueForPosition() {
		return this.specialValueForPosition = getRandom() ? -1 : 1;
	}

}
