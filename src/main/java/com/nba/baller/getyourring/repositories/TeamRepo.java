package com.nba.baller.getyourring.repositories;

import com.nba.baller.getyourring.models.Owner;
import com.nba.baller.getyourring.models.game.Team;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TeamRepo extends CrudRepository<Team, Integer> {

	@Query(value = "select t from Team t where t.owner=:deliveredOwner")
	List<Team> getTeamsByOwner(@Param("deliveredOwner")Owner deliveredOwner );

	@Query(value = "select t from Team t order by size(t.leftOpponents) desc")
	Page<Team> getMaxLeftOpponentsFromAllTeams(Pageable pageable);

}

