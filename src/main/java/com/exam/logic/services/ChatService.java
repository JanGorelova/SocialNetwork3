package com.exam.logic.services;

import com.exam.dao.ChatDAO;
import com.exam.models.Chat;
import com.exam.models.Message;
import lombok.RequiredArgsConstructor;

import java.time.ZoneId;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class ChatService {
    private final ChatDAO chatDAO;

    public List<Chat> getChats(Long userID, int offset, int limit, ZoneId zoneId) {
        return chatDAO.getChats(userID, offset, limit, zoneId);
    }

    public Message getLastShortMessage(Long chatID, ZoneId zoneId) {
        return chatDAO.getLastShortMessage(chatID, zoneId);
    }

    public List<Message> getMessages(Long userID, Integer offset, Integer limit, ZoneId userZoneId, Long userId) {
        return chatDAO.getMessages(userID,offset,limit,userZoneId, userId);
    }

    public void sendMessage(Message message) {
        chatDAO.sendMessage(message);
    }

    public Long countMessage(Long chatID) {
        return chatDAO.countMessage(chatID);
    }

    public Optional<Chat> getChatBeetwen(Long senderID, Long recipientID) {
        return chatDAO.getBetween(senderID,recipientID);
    }

    public void newChat(Chat chat) {
        chatDAO.create(chat);
    }
}