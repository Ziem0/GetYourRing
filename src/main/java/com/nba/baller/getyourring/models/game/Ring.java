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
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	@Column(nullable = false)
	@Temporal(TemporalType.DATE)
	private Date date;

	@OneToOne
	private Match match;

	@ManyToOne
	private Owner owner;

	public Ring() {
	}

	public Ring(Date date, Match match, Owner owner) {
		this.date = date;
		this.match = match;
		this.owner = owner;
	}
}

