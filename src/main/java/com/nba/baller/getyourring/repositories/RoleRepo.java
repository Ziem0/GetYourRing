package com.nba.baller.getyourring.repositories;

import com.nba.baller.getyourring.models.Roles;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepo extends CrudRepository<Roles, Integer> {
}
