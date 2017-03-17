package com.exam.logic.actions.chats;

import com.exam.logic.Action;
import com.exam.logic.services.ChatService;
import com.exam.logic.services.PhotoService;
import com.exam.models.Chat;
import com.exam.models.Message;
import com.exam.models.Photo;
import com.exam.models.User;
import lombok.extern.log4j.Log4j;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.time.ZoneId;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.exam.logic.Constants.*;

@Log4j
public class ChatListAction implements Action {

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        int offset = Optional.ofNullable(request.getParameter(OFFSET))
                .map(Integer::parseInt)
                .orElse(0);

        int limit = Optional.ofNullable(request.getParameter(LIMIT))
                .filter(s -> !s.isEmpty())
                .map(Integer::parseInt)
                .orElse(DEFAULT_LIMIT);

        ChatService chatService = (ChatService) request.getServletContext().getAttribute(CHAT_SERVICE);
        HttpSession session = request.getSession();
        User currentUser = (User) session.getAttribute(CURRENT_USER);

        ZoneId userZoneId = (ZoneId) Optional
                .ofNullable(session.getAttribute(USER_ZONE_ID))
                .orElseGet(() -> ZoneId.of("UTC"));

        List<Chat> chatList = chatService.getChats(currentUser.getId(), offset, limit, userZoneId);

        boolean hasNextPage = false;
        if (chatList.size() >= limit) {
            hasNextPage = true;
            if (chatList.isEmpty()) {
                hasNextPage = false;
                offset -= limit;
                chatList = chatService.getChats(currentUser.getId(), offset, limit, userZoneId);
            }
        }
        Map<Long, Message> chatIDtoMessage = chatList.stream()
                .map(Chat::getId)
                .map(id -> chatService.getLastShortMessage(id, userZoneId))
                .collect(Collectors.toMap(Message::getChatID,
                        Function.identity()));
        request.setAttribute(OFFSET, offset);
        request.setAttribute(LIMIT, limit);
        request.setAttribute(CHATS_LIST, chatList);
        request.setAttribute(MESSAGE_MAP, chatIDtoMessage);
        request.setAttribute(HAS_NEXT_PAGE, hasNextPage);

        return "/WEB-INF/jsp/chats/list.jsp";
    }
}