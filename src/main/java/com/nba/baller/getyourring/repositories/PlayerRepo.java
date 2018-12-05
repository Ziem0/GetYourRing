package com.nba.baller.getyourring.repositories;

import com.nba.baller.getyourring.models.game.Player;
import com.nba.baller.getyourring.models.game.Team;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlayerRepo extends CrudRepository<Player, Integer> {

	@Query(value = "select p from Player p where p.team=:deliveredTeam order by p.id asc")
	List<Player> getPlayersByTeam(@Param("deliveredTeam") Team team);


}
