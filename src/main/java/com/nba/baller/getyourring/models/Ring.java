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
public class Ring {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	@Column(nullable = false)
	@Temporal(TemporalType.DATE)
	private Date date;

	@OneToOne
	@Column(nullable = false)
	private Match match;

	@ManyToOne
	@Column(nullable = false)
	private User user;


	public Ring() {
	}

	public Ring(Date date, Match match, User user) {
		this.date = date;
		this.match = match;
		this.user = user;
	}
}

