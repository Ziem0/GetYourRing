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
public class Owner {

	@Column(unique = true,nullable = false)
	@Id
	private String nick;

	@Column(nullable = false)
	private String password;

	@Column(unique = true,nullable = false)
	private String email;

	public Owner() {
	}

	public Owner(String nick, String password, String email) {
		this.nick = nick;
		this.password = password;
		this.email = email;
	}

}
