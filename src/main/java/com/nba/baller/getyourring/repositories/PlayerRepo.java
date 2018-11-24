package com.nba.baller.getyourring.repositories;

import com.nba.baller.getyourring.models.Player;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlayerRepo extends CrudRepository<Player, Integer> {
}
