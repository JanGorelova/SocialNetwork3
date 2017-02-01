package com.exam.logic.actions.chats;

import com.exam.logic.Action;
import com.exam.logic.services.ChatService;
import com.exam.models.Chat;
import com.exam.models.User;
import lombok.SneakyThrows;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.exam.logic.Constants.CHAT_SERVICE;
import static com.exam.logic.Constants.CURRENT_USER;

public class GetPrivateChatAction implements Action {

    @Override
    @SneakyThrows
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        Long recipientID = Optional.ofNullable(request.getParameter("recipient_id"))
                .map(Long::parseLong)
                .orElseThrow(() -> new RuntimeException("Invalid recipient id"));

        ChatService chatService = (ChatService) request.getServletContext().getAttribute(CHAT_SERVICE);


        HttpSession session = request.getSession();
        User currentUser = (User) session.getAttribute(CURRENT_USER);
        Optional<Chat> chatOptional = chatService.getChatBeetwen(currentUser.getId(), recipientID);
        if (chatOptional.isPresent()) {
            response.sendRedirect("/chat/window?chat_id=" + chatOptional.get().getId());
        } else {
            List<Long> participantsID=new ArrayList<>();
            participantsID.add(currentUser.getId());
            participantsID.add(recipientID);
            Chat chat = Chat.builder()
                    .name("private")
                    .creatorID(currentUser.getId())
                    .lastUpdate(ZonedDateTime.now(ZoneId.of("UTC")))
                    .participantsID(participantsID)
                    .build();
            chatService.newChat(chat);
            chatOptional = chatService.getChatBeetwen(currentUser.getId(), recipientID);
            response.sendRedirect("/chat/window?chat_id=" + chatOptional.orElseThrow(RuntimeException::new).getId());
        }
        return null;
    }
}