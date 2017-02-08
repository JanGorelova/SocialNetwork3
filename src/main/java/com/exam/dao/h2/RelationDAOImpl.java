package com.exam.dao.h2;

import com.exam.connection_pool.ConnectionPool;
import com.exam.dao.DaoException;
import com.exam.dao.RelationDAO;
import com.exam.models.Relation;
import lombok.RequiredArgsConstructor;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import static com.exam.logic.Constants.*;

@RequiredArgsConstructor
public class RelationDAOImpl implements RelationDAO {
    private final ConnectionPool connectionPool;

    @Override
    public List<Long> getFriendsID(Long id, Integer offset, Integer limit) {
        List<Long> list;
        try (Connection connection = connectionPool.takeConnection()) {
            list = executeQuery(connection,
                    "SELECT recipient r_id FROM Relations WHERE sender=? AND type=3 UNION " +
                            "SELECT sender r_id FROM Relations WHERE recipient=? AND type=3 LIMIT ? OFFSET ?",
                    rs -> rs.getLong("r_id"),
                    id, id, limit, offset);
        } catch (SQLException e) {
            throw new DaoException(e);
        }
        return list;
    }

    @Override
    public Relation getBetween(Long sender, Long recipient) {
        List<Relation> list;
        try (Connection connection = connectionPool.takeConnection()) {
            list = executeQuery(connection,
                    "SELECT * FROM Relations WHERE sender=? AND recipient=? UNION " +
                            "SELECT * FROM Relations WHERE recipient=? AND sender=?",
                    rs -> {
                        Relation.RelationBuilder builder = Relation.builder()
                                .id(rs.getLong("id"));
                        if (rs.getLong("sender") != sender) {
                            builder.sender(recipient)
                                    .recipient(sender);
                            if (rs.getInt("type") == REQUEST)
                                builder.type(INCOMING);
                            else builder.type(rs.getInt("type"));
                        } else {
                            builder.sender(sender)
                                    .recipient(recipient)
                                    .type(rs.getInt("type"));
                        }
                        return builder.build();
                    },
                    sender, recipient, sender, recipient);
        } catch (SQLException e) {
            throw new DaoException(e);
        }
        return list.stream()
                .findFirst()
                .orElseGet(() -> Relation.builder()
                        .sender(sender)
                        .recipient(recipient)
                        .type(NEUTRAL)
                        .build());
    }

    @Override
    public void addFriend(Long sender, Long recipient) {
        int existType = getBetween(sender, recipient).getType();
        if (existType != INCOMING) throw new DaoException("Invalid relation type");
        try (Connection connection = connectionPool.takeConnection()) {
            connection.setAutoCommit(false);
            executeUpdate(connection,
                    "DELETE FROM Relations WHERE id=(SELECT id FROM Relations WHERE sender=? AND recipient=? " +
                            "UNION SELECT id FROM Relations WHERE recipient=? AND sender=?)",
                    sender, recipient, sender, recipient);
            executeUpdate(connection,
                    "INSERT INTO Relations (sender, recipient, type) VALUES (?, ?, ?)",
                    sender, recipient, FRIEND);
            connection.commit();
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public List<Long> getIncomingID(Long userID, int offset, int limit) {
        List<Long> list;
        try (Connection connection = connectionPool.takeConnection()) {
            list = executeQuery(connection,
                    "SELECT sender FROM Relations WHERE recipient=? AND type=1 LIMIT ? OFFSET ?",
                    rs -> rs.getLong("sender"),
                    userID, limit, offset);
        } catch (SQLException e) {
            throw new DaoException(e);
        }
        return list;
    }

    @Override
    public List<Long> getRequestID(Long userID, int offset, int limit) {
        List<Long> list;
        try (Connection connection = connectionPool.takeConnection()) {
            list = executeQuery(connection,
                    "SELECT recipient FROM Relations WHERE sender=? AND type=1 LIMIT ? OFFSET ?",
                    rs -> rs.getLong("recipient"),
                    userID, limit, offset);
        } catch (SQLException e) {
            throw new DaoException(e);
        }
        return list;
    }

    @Override
    public void create(Relation entity) {
        try (Connection connection = connectionPool.takeConnection()) {
            executeUpdate(connection,
                    "INSERT INTO Relations (sender, recipient, type) VALUES (?, ?, ?)",
                    entity.getSender(),
                    entity.getRecipient(),
                    entity.getType());
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public Optional<Relation> read(Long key) {
        Optional<Relation> relationOptional;
        try (Connection connection = connectionPool.takeConnection()) {

            relationOptional = executeQuery(connection,
                    "SELECT * FROM Relations WHERE id=?",
                    rs -> Relation.builder()
                            .id(rs.getLong("id"))
                            .sender(rs.getLong("sender"))
                            .recipient(rs.getLong("recipient"))
                            .type(rs.getInt("type"))
                            .build(),
                    key)
                    .stream()
                    .findFirst();
        } catch (SQLException e) {
            throw new DaoException(e);
        }
        return relationOptional;
    }

    @Override
    public void update(Relation entity) {
        // TODO: 27.01.2017 implementation
    }

    @Override
    public void delete(Long id) {
        try (Connection connection = connectionPool.takeConnection()) {
            executeUpdate(connection,
                    "DELETE FROM  Relations WHERE id=?",
                    id);
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }
}
