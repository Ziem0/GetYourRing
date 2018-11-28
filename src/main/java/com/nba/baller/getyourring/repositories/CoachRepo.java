package com.nba.baller.getyourring.repositories;

import com.nba.baller.getyourring.models.game.Coach;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CoachRepo extends CrudRepository<Coach, String> {
}
