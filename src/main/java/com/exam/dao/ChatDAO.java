package com.exam.dao;

import com.exam.models.Chat;
import com.exam.models.Message;

import java.time.ZoneId;
import java.util.List;
import java.util.Optional;

public interface ChatDAO extends BaseDAO<Chat, Long> {
    List<Message> getMessages(Long chatID, Integer offset, Integer limit, ZoneId userZoneId, Long userId);

    List<Chat> getChats(Long userID, int offset, int limit, ZoneId zoneId);

    Message getLastShortMessage(Long chatID, ZoneId zoneId);

    void sendMessage(Message message);

    Long countMessage(Long chatID);

    Optional<Chat> getBetween(Long senderID, Long recipientID);
}