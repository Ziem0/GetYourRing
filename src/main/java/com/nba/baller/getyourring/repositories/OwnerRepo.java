package com.nba.baller.getyourring.repositories;

import com.nba.baller.getyourring.models.Owner;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OwnerRepo extends CrudRepository<Owner, String> {

}
