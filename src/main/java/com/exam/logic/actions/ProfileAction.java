package com.exam.logic.actions;

import com.exam.logic.Action;
import com.exam.logic.services.ProfileService;
import com.exam.logic.services.UserService;
import com.exam.models.Profile;
import com.exam.models.User;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

import static com.exam.listeners.Initializer.PROFILE_SERVICE;
import static com.exam.listeners.Initializer.USER_SERVICE;
import static com.exam.logic.Constants.CURRENT_USER;

public class ProfileAction implements Action {

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        UserService userService = (UserService) request.getServletContext().getAttribute(USER_SERVICE);

        User user = Optional.ofNullable(request.getParameter("id"))
                .map(Long::parseLong)
                .flatMap(userService::getById)
                .orElse((User) request
                        .getSession()
                        .getAttribute(CURRENT_USER));

        request.setAttribute("user", user);

        ProfileService profileService = (ProfileService) request.getServletContext().getAttribute(PROFILE_SERVICE);
        Profile profile = profileService.getById(user.getId());
        request.setAttribute("profile", profile);

        return "/WEB-INF/jsp/profile/profile.jsp";
    }
}