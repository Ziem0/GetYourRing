package com.nba.baller.getyourring.repositories;

import com.nba.baller.getyourring.models.game.City;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CityRepo extends CrudRepository<City, String> {

}
