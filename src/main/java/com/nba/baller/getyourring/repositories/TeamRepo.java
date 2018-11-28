package com.nba.baller.getyourring.repositories;

import com.nba.baller.getyourring.models.Owner;
import com.nba.baller.getyourring.models.game.Team;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TeamRepo extends CrudRepository<Team, Integer> {

	@Query(value = "select t from Team t where t.owner=:deliveredOwner")
	Iterable<Team> getTeamsByOwner(@Param("deliveredOwner")Owner deliveredOwner );

}

