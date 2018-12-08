package com.nba.baller.getyourring.models;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Slf4j
@Entity
@Getter
@Setter
public class Owner {

	@Column(unique = true, nullable = false)
	@Id
	private String username;

	@Column(nullable = false)
	private String password;

	@Column(nullable = false)
	private Boolean enabled = true;

	@Column(unique = true, nullable = false)
	private String email;

	public Owner() {
	}

	public Owner(String username, String password,  String email) {
		this.username = username;
		this.password = passwordEncoder().encode(password);
		this.email = email;
	}

	public void setEncodedPassword() {
		this.password = this.passwordEncoder().encode(this.getPassword());
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

}
