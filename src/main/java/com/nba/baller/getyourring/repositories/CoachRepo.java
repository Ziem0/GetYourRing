package com.nba.baller.getyourring.repositories;

import com.nba.baller.getyourring.models.game.Coach;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CoachRepo extends CrudRepository<Coach, String> {

	@Query(value = "select c from Coach c where c=:deliveredCoach")
	Coach getCoachByTeam(@Param("deliveredCoach") Coach deliveredCoach);
}
