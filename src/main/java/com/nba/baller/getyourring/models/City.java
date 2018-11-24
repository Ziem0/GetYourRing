package com.nba.baller.getyourring.models;

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
public class City {

	@Id
	@Column(nullable = false)
	private String name;

	public City() {
	}

	public City(String name) {
		this.name = name;
	}
}
