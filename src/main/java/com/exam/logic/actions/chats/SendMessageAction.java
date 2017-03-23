package com.exam.logic.actions.chats;

import com.exam.logic.Action;
import com.exam.logic.services.ChatService;
import com.exam.models.Message;
import com.exam.models.User;
import lombok.SneakyThrows;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Optional;

import static com.exam.logic.Constants.*;

public class SendMessageAction implements Action {
    @Override
    @SneakyThrows
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        Long chatID = Optional.ofNullable(request.getParameter("chat_id"))
                .map(Long::parseLong)
                .orElseThrow(() -> new RuntimeException("Invalid chat id"));

        String text = Optional.ofNullable(request.getParameter("text"))
                .filter(s -> s.length() > 0)
                .orElse("(Empty message)");

        ChatService chatService = (ChatService) request.getServletContext().getAttribute(CHAT_SERVICE);
        HttpSession session = request.getSession();
        User currentUser = (User) session.getAttribute(CURRENT_USER);

        Message message = Message.builder()
                .senderID(currentUser.getId())
                .chatID(chatID)
                .text(text)
                .sendingTime(ZonedDateTime.ofInstant(Instant.now(),ZoneId.of("UTC")))
                .build();
        chatService.sendMessage(message);
        String referer = request.getHeader("referer");
        response.sendRedirect(referer != null ? referer : request.getContextPath());
        return null;
    }
}