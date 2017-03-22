package com.exam.dao.h2;

import com.exam.connection_pool.ConnectionPool;
import com.exam.dao.ChatDAO;
import com.exam.dao.DaoException;
import com.exam.models.Chat;
import com.exam.models.Message;
import lombok.RequiredArgsConstructor;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class ChatDAOImpl implements ChatDAO {
    private final ConnectionPool connectionPool;

    @Override
    public void create(Chat chat) {
        try (Connection connection = connectionPool.takeConnection()) {
            connection.setAutoCommit(false);
            try {
                executeUpdate(connection,
                        "INSERT INTO Chats (name, creator_id, description, last_update, start_time) VALUES (?,?,?,?,?)",
                        chat.getName(),
                        chat.getCreatorID(),
                        chat.getDescription(),
                        Timestamp.valueOf(chat.getLastUpdate().toLocalDateTime()),
                        Timestamp.valueOf(chat.getLastUpdate().toLocalDateTime()));
                Long chatID = (Long) executeQuery(connection,
                        "SELECT id FROM Chats WHERE creator_id=? ORDER BY id DESC LIMIT 1",
                        rs -> rs.getLong("id"),
                        chat.getCreatorID()).get(0);
                chat.getParticipantsID()
                        .forEach(id -> executeUpdate(connection,
                                "INSERT INTO Chat_participants (participant_id, chat_id) VALUES (?,?)",
                                id, chatID));
            } catch (Exception e) {
                connection.rollback();
                throw e;
            } finally {
                connection.setAutoCommit(true);
            }
            connection.commit();
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public Optional<Chat> read(Long key) {
        Optional<Chat> optional;
        try (Connection connection = connectionPool.takeConnection()) {

            optional = executeQuery(connection,
                    "SELECT * FROM Chats WHERE id=?",
                    rs -> {
                        Chat.ChatBuilder builder = Chat.builder()
                                .id(rs.getLong("id"))
                                .creatorID(rs.getLong("creator_id"))
                                .name(rs.getString("name"))
                                .lastUpdate(SQLtoTime("last_update", ZoneId.of("UTC"), rs).get())
                                .description(rs.getString("description"))
                                .startTime(SQLtoTime("start_time", ZoneId.of("UTC"), rs).get())
                                .lastRead(SQLtoTime("last_read", ZoneId.of("UTC"), rs)
                                        .orElseGet(() -> SQLtoTime("start_time", ZoneId.of("UTC"), rs).get()));
                        return builder.build();
                    },
                    key).stream().findFirst();
        } catch (SQLException e) {
            throw new DaoException(e);
        }
        return optional;
    }

    @Override
    public void update(Chat chat) {
        try (Connection connection = connectionPool.takeConnection()) {
            Timestamp lastUpdate = Timestamp.from(chat.getLastUpdate().toInstant());
            Timestamp startTime = Timestamp.from(chat.getStartTime().toInstant());
            executeUpdate(connection,
                    "UPDATE Chats SET name=?, creator_id=?, description=?, last_update=?, start_time=?  WHERE id=?",
                    chat.getName(),
                    chat.getCreatorID(),
                    chat.getDescription(),
                    lastUpdate,
                    startTime,
                    chat.getId());
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public void delete(Long id) {
        try (Connection connection = connectionPool.takeConnection()) {
            executeUpdate(connection,
                    "DELETE FROM Chats WHERE id=?",
                    id);
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public List<Message> getMessages(Long chatID, Integer offset, Integer limit, ZoneId userZoneId, Long userId) {
        List<Message> messageList;
        try (Connection connection = connectionPool.takeConnection()) {
            executeUpdate(connection,
                    "UPDATE Chat_Participants SET last_read=? WHERE chat_id=? AND participant_id=?",
                    Timestamp.from(Instant.now()), chatID, userId);
            messageList = executeQuery(connection,
                    "SELECT m.id, m.text, m.sender_id, m.chat_id, m.sending_time, u.first_name FROM Messages AS m " +
                            "LEFT JOIN Users AS u WHERE m.chat_id = ? " +
                            "AND u.id=m.sender_id ORDER BY m.sending_time DESC LIMIT ? OFFSET ?",
                    rs -> Message.builder()
                            .id(rs.getLong("id"))
                            .chatID(rs.getLong("chat_id"))
                            .senderID(rs.getLong("sender_id"))
                            .senderName(rs.getString("first_name"))
                            .sendingTime(ZonedDateTime.of(
                                    rs.getTimestamp("sending_time").toLocalDateTime(),
                                    userZoneId))
                            .text(rs.getString("text")).build(),
                    chatID, limit, offset);
            Collections.reverse(messageList);
        } catch (SQLException e) {
            throw new DaoException(e);
        }
        return messageList;
    }

    @Override
    public List<Chat> getChats(Long userID, int offset, int limit, ZoneId zoneId) {
        List<Chat> list;
        try (Connection connection = connectionPool.takeConnection()) {

            list = executeQuery(connection,
                    "SELECT c.*, cp.last_read FROM Chats AS c LEFT JOIN Chat_Participants AS cp " +
                            "WHERE c.id = cp.chat_id AND cp.participant_id=? AND c.last_update>c.start_time " +
                            "ORDER BY last_update DESC LIMIT ? OFFSET ?",
                    rs -> {
                        Chat.ChatBuilder builder = Chat.builder()
                                .id(rs.getLong("id"))
                                .creatorID(rs.getLong("creator_id"))
                                .name(rs.getString("name"))
                                .lastUpdate(SQLtoTime("last_update", zoneId, rs).get())
                                .description(rs.getString("description"))
                                .startTime(SQLtoTime("start_time", zoneId, rs).get())
                                .lastRead(SQLtoTime("last_read", zoneId, rs)
                                        .orElseGet(() -> SQLtoTime("start_time", zoneId, rs).get()));
                        return builder.build();
                    },
                    userID, limit, offset);
        } catch (SQLException e) {
            throw new DaoException(e);
        }
        return list;
    }

    private Optional<ZonedDateTime> SQLtoTime(String column, ZoneId zoneId, ResultSet rs) {
        try {
            return Optional
                    .ofNullable(rs.getTimestamp(column))
                    .map(Timestamp::toInstant)
                    .map(instant -> ZonedDateTime.ofInstant(instant, zoneId));
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public Message getLastShortMessage(Long chatID, ZoneId zoneId) {
        Message message;
        try (Connection connection = connectionPool.takeConnection()) {
            message = executeQuery(connection,
                    "SELECT m.id, m.text, m.sender_id, m.chat_id, m.sending_time, u.first_name FROM Messages AS m " +
                            "LEFT JOIN Users AS u WHERE m.chat_id = ? " +
                            "AND u.id=m.sender_id ORDER BY m.sending_time DESC LIMIT 1 OFFSET 0",
                    rs -> {
                        Message.MessageBuilder builder = Message.builder()
                                .id(rs.getLong("id"))
                                .chatID(rs.getLong("chat_id"))
                                .senderID(rs.getLong("sender_id"))
                                .senderName(rs.getString("first_name"))
                                .sendingTime(SQLtoTime("sending_time", zoneId, rs).get());

                        String text = rs.getString("text");
                        final int maxCharInMessage = 14;
                        if (text.length() > maxCharInMessage) {
                            text = text.substring(0, maxCharInMessage - 1) + "...";
                        }
                        builder.text(text);
                        return builder.build();
                    },
                    chatID)
                    .stream()
                    .findFirst()
                    .orElseGet(() -> Message.builder()
                            .chatID(chatID)
                            .build());
        } catch (SQLException e) {
            throw new DaoException(e);
        }
        return message;
    }

    @Override
    public void sendMessage(Message message) {
        try (Connection connection = connectionPool.takeConnection()) {
            try {
                executeUpdate(connection,
                        "INSERT INTO Messages (sender_id, chat_id, text, sending_time) VALUES (?, ?, ?, ?)",
                        message.getSenderID(),
                        message.getChatID(),
                        message.getText(),
                        Timestamp.valueOf(message.getSendingTime().toLocalDateTime()));
                executeUpdate(connection,
                        "UPDATE Chats SET last_update=? WHERE id=?",
                        Timestamp.valueOf(message.getSendingTime().toLocalDateTime()),
                        message.getChatID());
                connection.commit();
            } catch (RuntimeException e) {
                connection.rollback();
                throw e;
            }
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public Long countMessage(Long chatID) {
        Long count;
        try (Connection connection = connectionPool.takeConnection()) {
            count = executeQuery(connection,
                    "SELECT (SELECT COUNT(*) FROM Messages) - " +
                            "  (SELECT COUNT(*) FROM Messages where chat_id<?) -" +
                            "  (SELECT COUNT(*) FROM Messages where chat_id>?)" +
                            "AS count",
                    rs -> rs.getLong("count"),
                    chatID, chatID)
                    .get(0);
        } catch (SQLException e) {
            throw new DaoException(e);
        }
        return count;
    }

    @Override
    public Optional<Chat> getChatBetween(Long senderID, Long recipientID) {
        Optional<Chat> chatOptional;
        try (Connection connection = connectionPool.takeConnection()) {
            chatOptional = executeQuery(connection,
                    "SELECT * FROM Chats INNER JOIN (SELECT cp1.chat_id filtered_id FROM (SELECT chat_id FROM Chat_Participants " +
                            "WHERE participant_id=?) AS cp1 " +
                            "INNER JOIN (SELECT chat_id FROM Chat_Participants WHERE participant_id=?) AS cp2 ON cp1.chat_id=cp2.chat_id) " +
                            "AS sel_chat ON sel_chat.filtered_id=Chats.id " +
                            "AND Chats.name='private'",
                    rs -> Chat.builder()
                            .id(rs.getLong("id"))
                            .creatorID(rs.getLong("creator_id"))
                            .name(rs.getString("name"))
                            .description(rs.getString("description"))
                            .build(),
                    senderID, recipientID)
                    .stream().findFirst();
        } catch (SQLException e) {
            throw new DaoException(e);
        }
        return chatOptional;
    }
}