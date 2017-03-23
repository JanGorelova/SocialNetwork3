package com.exam.logic.actions;

import com.exam.logic.Action;
import com.exam.logic.services.UserService;
import com.exam.models.User;
import com.exam.util.Security;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.Optional;

import static com.exam.logic.Constants.*;
import static com.exam.servlets.ErrorHandler.ErrorCode.LOGIN_FAIL;

public class LoginAction implements Action {

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        UserService userService = (UserService) request.getServletContext().getAttribute(USER_SERVICE);
        String view;
        String login = request.getParameter("j_username");
        String password = Security.md5Hex(request.getParameter("j_password"));
        //устанавливаем тайм зону
        String offset = request.getParameter("time_zone");
        ZoneOffset zoneOffset = ZoneOffset.ofHours(-Integer.parseInt(offset) / 60);
        request.getSession().setAttribute(USER_ZONE_ID, ZoneId.ofOffset("UTC", zoneOffset));

        Optional<User> userOptional = userService.authorize(login, password);
        if (userOptional.isPresent()) {
            request.getSession().setAttribute(CURRENT_USER, userOptional.get());
            view = "/index.jsp";
        } else {
            request.setAttribute(ERROR_MSG, LOGIN_FAIL.getPropertyName());
            view = "/WEB-INF/jsp/not_auth/login.jsp";
        }
        return view;
    }
}