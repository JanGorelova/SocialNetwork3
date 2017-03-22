package com.exam.dao;

import com.exam.models.Chat;
import com.exam.models.Message;

import java.time.ZoneId;
import java.util.List;
import java.util.Optional;

public interface ChatDAO extends BaseDAO<Chat, Long> {
    /**
     * Метод вовращает сообщения отдельного, ограниченные параметрами
     * @param chatID идентификатор чата
     * @param offset начало отсчёта сообщений
     * @param limit ограничение количества выборки
     * @param userZoneId тайм зона пользователя
     * @param userId id пользователя
     * @return Список сообщений в чате
     */
    List<Message> getMessages(Long chatID, Integer offset, Integer limit, ZoneId userZoneId, Long userId);

    /**
     * Метод возвращает списк чатов
     * @param userID id пользователя
     * @param offset начало отсчёта записей
     * @param limit ограничение
     * @param zoneId временная зона
     * @return список чатов
     */
    List<Chat> getChats(Long userID, int offset, int limit, ZoneId zoneId);

    /**
     * Метод вовращает последнее сообщение из чата в кратком виде
     * @param chatID идентификатор чата
     * @param zoneId временная зона
     * @return Message с сокращённым текстом
     */
    Message getLastShortMessage(Long chatID, ZoneId zoneId);

    void sendMessage(Message message);

    /**
     * Метод производит подсчёт количества сообщений в чате
     * @param chatID идентификатор чата
     * @return количество сообщений в чате
     */
    Long countMessage(Long chatID);

    /**
     * Метод вовращает приватный чат между двумя людьми (диалог).
     * Собеседников можно менять местами в параметрах, результат один и тот же.
     * Если приватного чата нет, то Optional не будет содержать объекта.
     * @param senderID идентификатор первого собеседника
     * @param recipientID идентификатор второго собеседника
     * @return Optional объекта Chat
     */
    Optional<Chat> getChatBetween(Long senderID, Long recipientID);
}