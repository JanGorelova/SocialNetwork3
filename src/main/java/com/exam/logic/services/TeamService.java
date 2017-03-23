package com.exam.logic.services;

import com.exam.dao.TeamDAO;
import com.exam.models.Team;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class TeamService {
    private final TeamDAO teamDAO;


    public List<Team> getAllTeams() {
        return teamDAO.getAllTeams();
    }
}
