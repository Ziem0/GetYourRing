package com.nba.baller.getyourring.repositories;

import com.nba.baller.getyourring.models.Hall;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HallRepo extends CrudRepository<Hall,String> {
}
