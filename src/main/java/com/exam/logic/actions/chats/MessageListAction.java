package com.exam.logic.actions.chats;

import com.exam.logic.Action;
import com.exam.logic.services.ChatService;
import com.exam.models.Message;
import com.exam.models.User;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;

import static com.exam.logic.Constants.*;

public class MessageListAction implements Action {
    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        Long chatID = Optional.ofNullable(request.getParameter("chat_id"))
                .map(Long::parseLong)
                .orElseThrow(() -> new RuntimeException("Invalid chat id"));

        int offset = Optional.ofNullable(request.getParameter(OFFSET))
                .map(Integer::parseInt)
                .orElse(0);

        int limit = Optional.ofNullable(request.getParameter(LIMIT))
                .filter(s -> s.length() > 0)
                .map(Integer::parseInt)
                .orElse(DEFAULT_LIMIT);

        ChatService chatService = (ChatService) request.getServletContext().getAttribute(CHAT_SERVICE);
        HttpSession session = request.getSession();
        User currentUser = (User) session.getAttribute(CURRENT_USER);
        ZoneId userZoneId = (ZoneId) Optional
                .ofNullable(session.getAttribute(USER_ZONE_ID))
                .orElseGet(() -> ZoneId.of("UTC"));

        List<Message> messageList = chatService.getMessages(chatID, offset, limit, userZoneId, currentUser.getId());

        boolean hasNextPage = false;
        long totalMessages = chatService.countMessage(chatID);
        if (totalMessages > (limit + offset)) hasNextPage = true;

        request.setAttribute(OFFSET, offset);
        request.setAttribute(LIMIT, limit);
        request.setAttribute(MESSAGE_LIST, messageList);
        request.setAttribute(HAS_NEXT_PAGE, hasNextPage);

        return "/WEB-INF/jsp/chats/window.jsp";
    }
}