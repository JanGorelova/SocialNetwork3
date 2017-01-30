package com.exam.logic.actions.profile;

import com.exam.logic.Action;
import com.exam.logic.services.ProfileService;
import com.exam.logic.services.UserService;
import com.exam.models.Profile;
import com.exam.models.Relation;
import com.exam.models.User;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

import static com.exam.logic.Constants.*;

public class ProfileAction implements Action {

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        UserService userService = (UserService) request.getServletContext().getAttribute(USER_SERVICE);

        User currentUser = (User) request.getSession().getAttribute(CURRENT_USER);
        User user = Optional.ofNullable(request.getParameter("id"))
                .map(Long::parseLong)
                .flatMap(userService::getById)
                .map(user1 -> {
                    Relation relation = userService.getRelation(currentUser.getId(), user1.getId());
                    request.setAttribute(RELATION_TYPE, relation.getType());
                    return user1;
                })
                .orElse(currentUser);

        request.setAttribute("user", user);

        ProfileService profileService = (ProfileService) request.getServletContext().getAttribute(PROFILE_SERVICE);
        Profile profile = profileService.getById(user.getId());
        request.setAttribute("profile", profile);

        return "/WEB-INF/jsp/profile/profile.jsp";
    }
}