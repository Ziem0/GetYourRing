package com.nba.baller.getyourring.models;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigInteger;

@Getter
@Setter
@Entity
@Table(name = "spring_session")
public class GameSession {

	@Id
	@Column(name = "primary_id")
	public String id;
	@Column(name = "session_id")
	public String sessionId;
	@Column(name = "creation_time")
	public BigInteger creationTime;
	@Column(name = "last_access_time")
	public BigInteger lastAccessTime;
	@Column(name = "max_inactive_interval")
	public Integer maxInactiveInterval;
	@Column(name = "expiry_time")
	public BigInteger expiryTime;
	@Column(name = "principal_name")
	public String user;

	public GameSession() {
	}

	public GameSession(String id, String sessionId, BigInteger creationTime, BigInteger lastAccessTime, Integer maxInactiveInterval, BigInteger expiryTime, String user) {
		this.id = id;
		this.sessionId = sessionId;
		this.creationTime = creationTime;
		this.lastAccessTime = lastAccessTime;
		this.maxInactiveInterval = maxInactiveInterval;
		this.expiryTime = expiryTime;
		this.user = user;
	}
}
