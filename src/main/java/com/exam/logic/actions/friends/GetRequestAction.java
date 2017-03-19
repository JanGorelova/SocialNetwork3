package com.exam.logic.actions.friends;

import com.exam.logic.Action;
import com.exam.logic.services.PhotoService;
import com.exam.logic.services.UserService;
import com.exam.models.Photo;
import com.exam.models.User;
import lombok.extern.log4j.Log4j;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.exam.logic.Constants.*;

@Log4j
public class GetRequestAction implements Action {
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
        List<User> requestList = userService.getRequests(currentUser.getId(), offset, limit);
        boolean hasNextPage = false;
        if (requestList.size() >= limit) {
            hasNextPage = true;
            if (requestList.isEmpty()) {
                hasNextPage = false;
                offset -= limit;
                requestList = userService.getRequests(currentUser.getId(), offset, limit);
            }
        }

        PhotoService photoService = (PhotoService) request.getServletContext().getAttribute(PHOTO_SERVICE);
        Map<Long, String> minAvatars = requestList.stream()
                .map(User::getId)
                .map(photoService::getUserAva)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toMap(
                        Photo::getSender,
                        Photo::getLinkOfMinPhoto));
        request.setAttribute(MIN_AVATARS, minAvatars);
        log.debug(requestList.size());
        request.setAttribute(OFFSET, offset);
        request.setAttribute(LIMIT, limit);
        request.setAttribute(USER_LIST, requestList);
        request.setAttribute(HAS_NEXT_PAGE, hasNextPage);
        return "/WEB-INF/jsp/friends/request.jsp";
    }
}