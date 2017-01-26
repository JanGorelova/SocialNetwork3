package com.exam.dao;

import com.exam.models.Team;

import java.util.Optional;

public interface TeamDAO extends BaseDAO<Team,Long>{
    Optional<Team> getByName(String name);
}