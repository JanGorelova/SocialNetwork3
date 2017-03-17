package com.exam.logic.actions.friends;

import com.exam.logic.Action;
import com.exam.logic.services.PhotoService;
import com.exam.logic.services.UserService;
import com.exam.models.Chat;
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
public class FriendsListAction implements Action {
    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        //   int offset = request.getParameter("offset") == null ? 0 : Integer.parseInt(request.getParameter("offset"));
        int offset = Optional.ofNullable(request.getParameter(OFFSET))
                .map(Integer::parseInt)
                .orElse(0);

        int limit = Optional.ofNullable(request.getParameter(LIMIT))
                .filter(s -> s.length() > 0)
                .map(Integer::parseInt)
                .orElse(DEFAULT_LIMIT);


        UserService userService = (UserService) request.getServletContext().getAttribute(USER_SERVICE);
        User currentUser = (User) request.getSession().getAttribute(CURRENT_USER);
        List<User> friendsList = userService.getFriends(currentUser.getId(), offset, limit);
        boolean hasNextPage = false;
        if (friendsList.size() >= limit) {
            hasNextPage = true;
            if (friendsList.isEmpty()) {
                hasNextPage = false;
                offset -= limit;
                friendsList = userService.getFriends(currentUser.getId(), offset, limit);
            }
        }

        PhotoService photoService = (PhotoService) request.getServletContext().getAttribute(PHOTO_SERVICE);
        Map<Long, String> minAvatars = friendsList.stream()
                .map(User::getId)
                .map(photoService::getUserAva)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toMap(
                        Photo::getSender,
                        Photo::getLinkOfMinPhoto));
        request.setAttribute(MIN_AVATARS,minAvatars);

        request.setAttribute(OFFSET, offset);
        request.setAttribute(LIMIT, limit);
        request.setAttribute(FRIENDS_LIST, friendsList);
        request.setAttribute(HAS_NEXT_PAGE, hasNextPage);
        log.debug("getting friends list");
        return "/WEB-INF/jsp/friends/list.jsp";
    }
}