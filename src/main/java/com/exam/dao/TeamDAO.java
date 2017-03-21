package com.exam.dao;

import com.exam.models.Team;

import java.util.Optional;

public interface TeamDAO extends BaseDAO<Team,Long>{
    /**
     * Метод вовращает объект отряда, произведя поиск по название
     * @param name название отряда
     */
    Optional<Team> getByName(String name);
}