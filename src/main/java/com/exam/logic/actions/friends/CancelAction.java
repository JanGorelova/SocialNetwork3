package com.exam.logic.actions.friends;

import com.exam.logic.Action;
import com.exam.logic.actions.InvalidRequestParameter;
import com.exam.logic.services.UserService;
import com.exam.models.User;
import lombok.SneakyThrows;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

import static com.exam.logic.Constants.CURRENT_USER;
import static com.exam.logic.Constants.USER_SERVICE;

public class CancelAction implements Action {

    @Override
    @SneakyThrows
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        Long recipientID = Optional.ofNullable(request.getParameter("user_id"))
                .map(Long::parseLong)
                .orElseThrow(() -> new InvalidRequestParameter("Invalid user id"));

        UserService userService = (UserService) request.getServletContext().getAttribute(USER_SERVICE);
        User currentUser = (User) request.getSession().getAttribute(CURRENT_USER);
        userService.deleteRelation(currentUser.getId(), recipientID);

        String referer = request.getHeader("referer");
        response.sendRedirect(referer != null ? referer : request.getContextPath());

        return null;
    }
}