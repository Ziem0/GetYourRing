package com.nba.baller.getyourring.repositories;

import com.nba.baller.getyourring.models.Team;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TeamRepo extends CrudRepository<Team, Integer> {
}
