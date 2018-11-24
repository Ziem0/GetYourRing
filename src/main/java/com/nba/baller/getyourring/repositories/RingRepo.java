package com.nba.baller.getyourring.repositories;

import com.nba.baller.getyourring.models.Ring;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RingRepo extends CrudRepository<Ring, Integer> {
}
