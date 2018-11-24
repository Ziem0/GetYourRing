package com.nba.baller.getyourring.repositories;

import com.nba.baller.getyourring.models.Match;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MatchRepo extends CrudRepository<Match, Integer> {
}
