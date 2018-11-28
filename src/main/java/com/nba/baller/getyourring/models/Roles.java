package com.nba.baller.getyourring.models;

import com.nba.baller.getyourring.helpers.Role;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
public class Roles {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	@OneToOne
	private Owner owner;

	@Enumerated(EnumType.STRING)
	private Role role;

	public Roles(Owner owner, Role role) {
		this.owner = owner;
		this.role = role;
	}

	public Roles() {
	}
}
