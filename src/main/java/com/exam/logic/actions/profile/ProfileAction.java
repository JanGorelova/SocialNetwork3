package com.exam.logic.actions.profile;

import com.exam.logic.Action;
import com.exam.logic.services.PhotoService;
import com.exam.logic.services.PostService;
import com.exam.logic.services.ProfileService;
import com.exam.logic.services.UserService;
import com.exam.models.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

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

        PhotoService photoService = (PhotoService) request.getServletContext().getAttribute(PHOTO_SERVICE);
        Optional<Photo> avaOptional = photoService.getUserAva(profile.getId());
        avaOptional.ifPresent(ava -> request.setAttribute("avatar", ava));

        int offset = Optional.ofNullable(request.getParameter(OFFSET))
                .map(Integer::parseInt)
                .orElse(0);
        request.setAttribute(OFFSET, offset);

        int limit = Optional.ofNullable(request.getParameter(LIMIT))
                .filter(s -> s.length() > 0)
                .map(Integer::parseInt)
                .orElse(DEFAULT_LIMIT);
        request.setAttribute(LIMIT, limit);

        PostService postService = (PostService) request.getServletContext().getAttribute(POST_SERVICE);
        List<Post> postList = postService.getPosts(profile.getId(), offset, limit);
        request.setAttribute(POST_LIST, postList);

        boolean hasNextPage = postService.hasNextPage(profile.getId(), offset, limit);
        request.setAttribute(HAS_NEXT_PAGE, hasNextPage);

        Map<Long, User> userMap = postList.stream()
                .map(Post::getSender)
                .map(userService::getById)
                .map(Optional::get)
                .collect(Collectors.toMap(
                        User::getId,
                        s -> s,
                        (id1, id2) -> id1));
        request.setAttribute(USER_MAP, userMap);

        Map<Long, String> minAvatars = postList.stream()
                .map(Post::getSender)
                .map(photoService::getUserAva)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toMap(
                        Photo::getSender,
                        Photo::getLinkOfMinPhoto,
                        (f1, f2) -> f1));
        request.setAttribute(MIN_AVATARS, minAvatars);

        return "/WEB-INF/jsp/profile/profile.jsp";
    }
}