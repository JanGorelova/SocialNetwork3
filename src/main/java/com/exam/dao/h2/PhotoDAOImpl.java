package com.exam.dao.h2;

import com.exam.connection_pool.ConnectionPool;
import com.exam.dao.DaoException;
import com.exam.dao.PhotoDAO;
import com.exam.models.Photo;
import lombok.RequiredArgsConstructor;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Optional;

@RequiredArgsConstructor
public class PhotoDAOImpl implements PhotoDAO {
    private final ConnectionPool connectionPool;

    @Override
    public void create(Photo photo) {
        if (photo.isAvatar()) {
            //если это аватарка, то необходимо затереть старую запись
            try (Connection connection = connectionPool.takeConnection()) {
                try {
                    connection.setAutoCommit(false);
                    executeUpdate(connection,
                            "DELETE FROM Photos WHERE sender=? AND avatar=true",
                            photo.getSender());
                    executeUpdate(connection,
                            "INSERT INTO Photos (sender, time, avatar, link) VALUES(?,?,?,?)",
                            photo.getSender(),
                            Timestamp.from(photo.getTime()),
                            photo.isAvatar(),
                            photo.getLink());
                    connection.commit();
                } catch (SQLException e){
                    connection.rollback();
                    throw e;
                }
                finally {
                    connection.setAutoCommit(true);
                }
            } catch (SQLException e) {
                throw new DaoException(e);
            }
        } else {
            //если обычная фотка, то просто добавляем запись в бд
            try (Connection connection = connectionPool.takeConnection()) {
                executeUpdate(connection,
                        "INSERT INTO Photos (sender, time, avatar, link) VALUES(?,?,?,?)",
                        photo.getSender(),
                        Timestamp.from(photo.getTime()),
                        photo.isAvatar(),
                        photo.getLink());
            } catch (SQLException e) {
                throw new DaoException(e);
            }
        }
    }

    @Override
    public Optional<Photo> read(Long key) {
        Optional<Photo> relationOptional;
        try (Connection connection = connectionPool.takeConnection()) {
            relationOptional = executeQuery(connection,
                    "SELECT * FROM Photos WHERE id=?",
                    rs -> Photo.builder()
                            .id(rs.getLong("id"))
                            .sender(rs.getLong("sender"))
                            .time(rs.getTimestamp("time").toInstant())
                            .avatar(rs.getBoolean("avatar"))
                            .link(rs.getString("link"))
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
    public void update(Photo photo) {
        try (Connection connection = connectionPool.takeConnection()) {
            executeUpdate(connection,
                    "UPDATE Photos SET sender=?, time=?, avatar=?, link=?  WHERE id=?",
                    photo.getSender(),
                    Timestamp.from(photo.getTime()),
                    photo.isAvatar(),
                    photo.getLink(),
                    photo.getId());
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public void delete(Long id) {
        try (Connection connection = connectionPool.takeConnection()) {
            executeUpdate(connection,
                    "DELETE FROM Photos WHERE id=?",
                    id);
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public Optional<Photo> getAvaByUserId(Long id) {
        Optional<Photo> relationOptional;
        try (Connection connection = connectionPool.takeConnection()) {
            relationOptional = executeQuery(connection,
                    "SELECT * FROM Photos WHERE sender=? and avatar=true",
                    rs -> Photo.builder()
                            .id(rs.getLong("id"))
                            .sender(rs.getLong("sender"))
                            .time(rs.getTimestamp("time").toInstant())
                            .avatar(rs.getBoolean("avatar"))
                            .link(rs.getString("link"))
                            .build(),
                    id)
                    .stream()
                    .findFirst();
        } catch (SQLException e) {
            throw new DaoException(e);
        }
        return relationOptional;
    }
}