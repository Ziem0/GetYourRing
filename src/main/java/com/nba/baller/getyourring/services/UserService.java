package com.nba.baller.getyourring.services;

import com.nba.baller.getyourring.helpers.Role;
import com.nba.baller.getyourring.models.GameSession;
import com.nba.baller.getyourring.models.Owner;
import com.nba.baller.getyourring.models.Roles;
import com.nba.baller.getyourring.repositories.OwnerRepo;
import com.nba.baller.getyourring.repositories.RoleRepo;
import com.nba.baller.getyourring.repositories.SessionRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

	final
	OwnerRepo ownerRepo;
	final
	RoleRepo roleRepo;
	final
	SessionRepo sessionRepo;

	@Autowired
	public UserService(OwnerRepo ownerRepo, RoleRepo roleRepo, SessionRepo sessionRepo) {
		this.ownerRepo = ownerRepo;
		this.roleRepo = roleRepo;
		this.sessionRepo = sessionRepo;
	}

	//check if given sessionId is correct; sessionId != primaryId
	public Owner getOwnerBySessionId(String sessionId) {
		String username = sessionRepo.getSessionBySessionId(sessionId).user;
		return ownerRepo.getOwnerByName(username);
	}

	//check if given sessionId is correct; sessionId != primaryId
	public GameSession getSessionBySessionId(String sessionId) {
		return sessionRepo.getSessionBySessionId(sessionId);
	}

	public void saveOwner(Owner owner) {
		owner.setEncodedPassword();
		ownerRepo.save(owner);

		Roles roles = new Roles(owner, Role.ROLE_USER);
		roleRepo.save(roles);
	}
}
