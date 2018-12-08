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

	@Query(value = "select r.owner || '' , count(r) from Ring r group by r.owner")
	List<Object[]> getTop10Data();

}
