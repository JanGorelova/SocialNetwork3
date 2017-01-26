package com.exam.logic.actions;

import com.exam.logic.Action;
import com.exam.logic.services.UserService;
import com.exam.models.User;
import com.exam.util.Security;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

import static com.exam.listeners.Initializer.USER_SERVICE;
import static com.exam.logic.Constants.CURRENT_USER;
import static com.exam.logic.Constants.ERROR_MSG;
import static com.exam.servlets.ErrorHandler.ErrorCode.LOGIN_FAIL;

public class LoginAction implements Action {

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        UserService userService = (UserService) request.getServletContext().getAttribute(USER_SERVICE);
        String view;
        String login = request.getParameter("j_username");
        String password = Security.md5Hex(request.getParameter("j_password"));

        Optional<User> userOptional = userService.authorize(login, password);
        if (userOptional.isPresent()) {
            request.getSession().setAttribute(CURRENT_USER, userOptional.get());
            view = "/index.jsp";
        } else {
            request.setAttribute(ERROR_MSG, LOGIN_FAIL.getPropertyName());
            view = "/WEB-INF/jsp/login.jsp";
        }
        return view;
    }
}