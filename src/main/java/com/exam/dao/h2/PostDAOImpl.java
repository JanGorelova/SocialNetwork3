package com.exam.dao.h2;

import com.exam.connection_pool.ConnectionPool;
import com.exam.dao.DaoException;
import com.exam.dao.PostDAO;
import com.exam.models.Post;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Log4j
public class PostDAOImpl implements PostDAO {
    private final ConnectionPool connectionPool;

    @Override
    public void create(Post entity) {
        try (Connection connection = connectionPool.takeConnection()) {
            executeUpdate(connection,
                    "INSERT INTO Posts (sender,recipient, time, message) VALUES(?,?,?,?)",
                    entity.getSender(),
                    entity.getRecipient(),
                    Timestamp.from(entity.getTime()),
                    entity.getMessage());
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public Optional<Post> read(Long key) {
        Optional<Post> relationOptional;
        try (Connection connection = connectionPool.takeConnection()) {
            relationOptional = executeQuery(connection,
                    "SELECT * FROM Posts WHERE id=?",
                    rs -> Post.builder()
                            .id(rs.getLong("id"))
                            .sender(rs.getLong("sender"))
                            .time(rs.getTimestamp("time").toInstant())
                            .recipient(rs.getLong("recipient"))
                            .message(rs.getString("message"))
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
    public void update(Post entity) {
        try (Connection connection = connectionPool.takeConnection()) {
            executeUpdate(connection,
                    "UPDATE Posts SET sender=?, recipient=?, time=?, message=?  WHERE id=?",
                    entity.getSender(),
                    entity.getRecipient(),
                    Timestamp.from(entity.getTime()),
                    entity.getMessage(),
                    entity.getId());
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public void delete(Long id) {
        try (Connection connection = connectionPool.takeConnection()) {
            executeUpdate(connection,
                    "DELETE FROM Posts WHERE id=?",
                    id);
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public List<Post> getByRecipient(Long id, int offset, int limit) {
        List<Post> list;
        try (Connection connection = connectionPool.takeConnection()) {
            list = executeQuery(connection,
                    "SELECT * FROM Posts WHERE recipient=? ORDER BY time DESC LIMIT ? OFFSET ?",
                    rs -> Post.builder()
                            .id(rs.getLong("id"))
                            .sender(rs.getLong("sender"))
                            .time(rs.getTimestamp("time").toInstant())
                            .recipient(rs.getLong("recipient"))
                            .message(rs.getString("message"))
                            .build(),
                    id,limit,offset);
        } catch (SQLException e) {
            throw new DaoException(e);
        }
        return list;
    }

    @Override
    public long getRows(Long id) {
        long rows;
        try (Connection connection = connectionPool.takeConnection()) {
            rows = executeQuery(connection,
                    "SELECT COUNT (*) FROM Posts WHERE recipient=?",
                    rs -> rs.getLong(1),
                    id).get(0);
        } catch (SQLException e) {
            throw new DaoException(e);
        }
        return rows;
    }
}