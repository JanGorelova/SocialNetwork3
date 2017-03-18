package com.exam.logic.actions.post;

import com.exam.logic.Action;
import com.exam.logic.services.PostService;
import com.exam.models.Post;
import com.exam.models.User;
import lombok.SneakyThrows;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.time.Instant;
import java.util.Optional;

import static com.exam.logic.Constants.CURRENT_USER;
import static com.exam.logic.Constants.POST_SERVICE;

public class NewPost implements Action {

    @SneakyThrows
    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        Long recipient = Optional.ofNullable(request.getParameter("recipient"))
                .map(Long::parseLong)
                .orElseThrow(() -> new RuntimeException("Invalid recipient id"));

        String text = Optional.ofNullable(request.getParameter("text"))
                .filter(s -> s.length() > 0)
                .orElse("(Empty message)");

        PostService postService = (PostService) request.getServletContext().getAttribute(POST_SERVICE);

        HttpSession session = request.getSession();
        User currentUser = (User) session.getAttribute(CURRENT_USER);

        Post post = Post.builder()
                .recipient(recipient)
                .message(text)
                .sender(currentUser.getId())
                .time(Instant.now())
                .build();
        postService.createPost(post);
        String referer = request.getHeader("referer");
        response.sendRedirect(referer != null ? referer : request.getContextPath());
        return null;
    }
}
