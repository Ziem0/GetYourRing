package com.nba.baller.getyourring.repositories;

import com.nba.baller.getyourring.models.Owner;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface OwnerRepo extends CrudRepository<Owner, String> {

	@Query(value = "select o from Owner o where o.username= :deliveredUsername")
	Owner getOwnerByName(@Param("deliveredUsername") String deliveredUsername);



}
