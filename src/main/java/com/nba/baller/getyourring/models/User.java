package com.nba.baller.getyourring.models;


import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.List;

@Slf4j
@Entity
@Getter
@Setter
public class User {

	@Id
	@Column(unique = true,nullable = false)
	private String nick;

	@Column(nullable = false)
	private String password;

	@Column(unique = true,nullable = false)
	private String email;

	@OneToMany(targetEntity = Ring.class, mappedBy = "user")
	private List<Ring> rings;

	public User() {
	}

	public User(String nick, String password, String email, List<Ring> rings) {
		this.nick = nick;
		this.password = password;
		this.email = email;
		this.rings = rings;
	}



}
