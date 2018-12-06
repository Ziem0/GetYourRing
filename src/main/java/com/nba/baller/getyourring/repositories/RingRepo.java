package com.nba.baller.getyourring.repositories;

import com.nba.baller.getyourring.models.Owner;
import com.nba.baller.getyourring.models.game.Ring;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RingRepo extends CrudRepository<Ring, Integer> {

	@Query(value = "select r from Ring r where r.owner=:deliveredOwner")
	List<Ring> getRingsByOwner(@Param("deliveredOwner") Owner deliveredOwner);
}
