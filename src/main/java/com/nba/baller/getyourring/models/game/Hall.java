package com.nba.baller.getyourring.models.game;


import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Slf4j
@Entity
@Getter
@Setter
public class Hall {

	@Id
	@Column(nullable = false)
	private String name;

	public Hall() {
	}

	public Hall(String name) {
		this.name = name;
	}
}
