package com.exam.logic.actions;

import com.exam.logic.Action;
import com.exam.logic.services.UserService;
import com.exam.logic.services.Validator;
import com.exam.models.User;
import com.exam.servlets.ErrorHandler;
import com.exam.util.Security;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.exam.listeners.Initializer.USER_SERVICE;
import static com.exam.logic.Constants.CURRENT_USER;
import static com.exam.logic.Constants.ERROR_MSG;

public class RegistrationAction implements Action {
    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        String firstName = request.getParameter("first_name");
        String lastName = request.getParameter("last_name");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String passwordConfirm = request.getParameter("password_confirm");
        Integer gender = request.getParameter("gender").equals("male") ? User.GENDER_MALE : User.GENDER_FEMALE;
        Validator.ValidCode validCode = Validator.validateRegistration(
                firstName, lastName, email, password, passwordConfirm);

        if (validCode != Validator.ValidCode.SUCCESS) {
            request.setAttribute(ERROR_MSG, validCode.getPropertyName());
            return "/WEB-INF/jsp/registration.jsp";
        } else {
            UserService userService = (UserService) request.getServletContext().getAttribute(USER_SERVICE);
            User user = User.builder()
                    .email(email)
                    .password(Security.md5Hex(password))
                    .firstName(firstName)
                    .lastName(lastName)
                    .gender(gender)
                    .role(User.ROLE_USER)
                    .build();
            ErrorHandler.ErrorCode errorCode = userService.register(user);
            if (errorCode != null) {
                request.setAttribute(ERROR_MSG, errorCode.getPropertyName());
                return "/WEB-INF/jsp/registration.jsp";
            } else {
                request.getSession().setAttribute(CURRENT_USER,userService.getSafeUser(user));
                return "/index.jsp";
            }
        }
    }
}