package com.exam.logic.actions.post;

import com.exam.logic.Action;
import com.exam.logic.services.PostService;
import com.exam.models.Post;
import com.exam.models.User;
import lombok.SneakyThrows;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Optional;

import static com.exam.logic.Constants.CURRENT_USER;
import static com.exam.logic.Constants.POST_SERVICE;

/**
 * Created by Vasiliy Bobkov on 21.03.2017.
 */
public class PostCleaner implements Action {
    @SneakyThrows
    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        Long postId = Optional.ofNullable(request.getParameter("post_id"))
                .map(Long::parseLong)
                .orElseThrow(() -> new RuntimeException("Invalid post id"));

        PostService postService = (PostService) request.getServletContext().getAttribute(POST_SERVICE);

        HttpSession session = request.getSession();
        User currentUser = (User) session.getAttribute(CURRENT_USER);
        Optional<Post> postOptional = postService.getById(postId);
        long userId = currentUser.getId();
        Post post = postOptional.filter(p -> p.getSender() == userId || p.getRecipient() == userId)
                .orElseThrow(() -> new RuntimeException(
                        "User " + currentUser.getId() + " are trying to delete post " + postId + " without right"));
        postService.deletePost(post.getId());
        String referer = request.getHeader("referer");
        response.sendRedirect(referer != null ? referer : request.getContextPath());
        return null;
    }
}