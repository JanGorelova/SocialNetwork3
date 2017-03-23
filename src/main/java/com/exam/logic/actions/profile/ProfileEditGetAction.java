package com.exam.logic.actions.profile;

import com.exam.logic.Action;
import com.exam.logic.services.ProfileService;
import com.exam.logic.services.TeamService;
import com.exam.models.Profile;
import com.exam.models.Team;
import com.exam.models.User;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

import static com.exam.logic.Constants.*;

public class ProfileEditGetAction implements Action {

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        ProfileService profileService = (ProfileService) request.getServletContext().getAttribute(PROFILE_SERVICE);
        User currentUser = (User) request.getSession().getAttribute(CURRENT_USER);
        Profile profile = profileService.getById(currentUser.getId());
        request.setAttribute(PROFILE, profile);

        TeamService teamService = (TeamService) request.getServletContext().getAttribute(TEAM_SERVICE);
        List<Team> teamList = teamService.getAllTeams();
        request.setAttribute(TEAM_LIST, teamList);
        return "/WEB-INF/jsp/profile/edit.jsp";
    }
}