package com.nba.baller.getyourring.repositories;

import com.nba.baller.getyourring.models.GameSession;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface SessionRepo extends CrudRepository<GameSession, String> {

	@Query(value = "select s from GameSession s where s.sessionId = :deliveredSessionId")
	GameSession checkIfLogged(@Param("deliveredSessionId")String deliveredSessionId);
}
