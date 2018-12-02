package com.nba.baller.getyourring.services;

import com.nba.baller.getyourring.helpers.Role;
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

	/**
	 * check if given sessionId is correct
	 * warning:sessionId != primaryId
	 * @param sessionId
	 * @return owner -> if owner has account
	 */
	public Owner getOwnerBySessionId(String sessionId) {
		String username = sessionRepo.getSessionBySessionId(sessionId).getUser();
		return ownerRepo.getOwnerByName(username);
	}

	/**
	 * update owner to database after password encode
	 * referral ROLE_USER to given owner
	 * save role to database
	 * @param owner
	 */
	public void saveOwner(Owner owner) {
		owner.setEncodedPassword();
		ownerRepo.save(owner);

		Roles roles = new Roles(owner, Role.ROLE_USER);
		roleRepo.save(roles);
	}





//check if given sessionId is correct; sessionId != primaryId
//	public GameSession getSessionBySessionId(String sessionId) {
//		return sessionRepo.getSessionBySessionId(sessionId);
//	}

}
