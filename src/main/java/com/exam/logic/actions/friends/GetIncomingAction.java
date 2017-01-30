package com.exam.logic.actions.friends;

import com.exam.logic.Action;
import com.exam.logic.services.UserService;
import com.exam.models.User;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Optional;

import static com.exam.logic.Constants.*;

public class GetIncomingAction implements Action {
    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        int offset = Optional.ofNullable(request.getParameter(OFFSET))
                .map(Integer::parseInt)
                .orElse(0);

        int limit = Optional.ofNullable(request.getParameter(LIMIT))
                .filter(s -> s.length() > 0)
                .map(Integer::parseInt)
                .orElse(DEFAULT_LIMIT);

        UserService userService = (UserService) request.getServletContext().getAttribute(USER_SERVICE);
        User currentUser = (User) request.getSession().getAttribute(CURRENT_USER);
        List<User> incomingList = userService.getIncomings(currentUser.getId(), offset, limit);
        boolean hasNextPage = false;
        if (incomingList.size() >= limit) {
            hasNextPage = true;
            if (incomingList.isEmpty()) {
                hasNextPage = false;
                offset -= limit;
                incomingList = userService.getIncomings(currentUser.getId(), offset, limit);
            }
        }


        request.setAttribute(OFFSET, offset);
        request.setAttribute(LIMIT, limit);
        request.setAttribute(USER_LIST, incomingList);
        request.setAttribute(HAS_NEXT_PAGE, hasNextPage);
        return "/WEB-INF/jsp/friends/incoming.jsp";
    }
}