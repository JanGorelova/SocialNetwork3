package com.exam.logic.actions.friends;

import com.exam.logic.Action;
import com.exam.logic.services.UserService;
import com.exam.models.User;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.exam.logic.Constants.*;
import static com.exam.servlets.ErrorHandler.ErrorCode.USER_NOT_FOUND;
import static com.exam.util.NameNormalizer.normalize;

public class SearchFriendsAction implements Action {
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
        String[] names = Optional.ofNullable(request.getParameter("names"))
                // .map(NameNormalizer::multiNormalize)
                .map(n -> n.split(" "))
                .orElse(new String[]{});
        //names.map()
        List<User> userList = new ArrayList<>();
        switch (names.length) {
            case 1:
                userList = userService.getByName(normalize(names[0]), offset, limit);
                request.setAttribute("userList", userList);
                break;
            case 2:
                userList = userService.getByNames(normalize(names[0]), normalize(names[1]), offset, limit);
                request.setAttribute("userList", userList);
                break;
        }
        if (names.length > 0 && userList.isEmpty()) {
            request.setAttribute(ERROR_MSG, USER_NOT_FOUND.getPropertyName());
        }
        return "/WEB-INF/jsp/friends/search.jsp";
    }
}