package com.exam.dao.h2;

import com.exam.connection_pool.ConnectionPool;
import com.exam.dao.DaoException;
import com.exam.dao.ResultSetProcessor;
import com.exam.dao.UserDAO;
import com.exam.models.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Log4j
@RequiredArgsConstructor
public class UserDAOImpl implements UserDAO {
    private final ConnectionPool connectionPool;

    private final ResultSetProcessor<User> userBuilderFromRS = (rs) -> User.builder()
            .id(rs.getLong("id"))
            .email(rs.getString("email"))
            .password(rs.getString("password"))
            .firstName(rs.getString("first_name"))
            .lastName(rs.getString("last_name"))
            .gender(rs.getInt("gender"))
            .role(rs.getInt("role"))
            .build();

    @Override
    public void create(User entity) {
        try (Connection connection = connectionPool.takeConnection()) {
            executeUpdate(connection,
                    "INSERT INTO Users (email, password, first_name, last_name, gender, role) VALUES (?, ?, ?, ?, ?, ?)",
                    entity.getEmail(),
                    entity.getPassword(),
                    entity.getFirstName(),
                    entity.getLastName(),
                    entity.getGender(),
                    entity.getRole());
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public Optional<User> read(Long key) {
        Optional<User> userOptional;
        try (Connection connection = connectionPool.takeConnection()) {
            //noinspection unchecked
            userOptional = executeQuery(connection,
                    "SELECT * FROM Users WHERE id=?",
                    userBuilderFromRS,
                    key)
                    .stream()
                    .findFirst();
        } catch (SQLException e) {
            throw new DaoException(e);
        }
        return userOptional;
    }

    @Override
    public void update(User entity) {
        try (Connection connection = connectionPool.takeConnection()) {
            executeUpdate(connection,
                    "UPDATE Users SET email=?, password=?, first_name=?, last_name=?, gender=?, role=? WHERE id=?",
                    entity.getEmail(),
                    entity.getPassword(),
                    entity.getFirstName(),
                    entity.getLastName(),
                    entity.getGender(),
                    entity.getRole(),
                    entity.getId());
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public void delete(Long id) {
//        try (Connection connection = connectionPool.takeConnection()) {
//            executeUpdate(connection,
//                    "DELETE Users , Profiles  FROM messages  INNER JOIN usersmessages  \n" +
//                            "WHERE messages.messageid= usersmessages.messageid and messages.messageid = '1'"
//                    "DELETE FROM Users WHERE id=?",
//                    id);
//        } catch (SQLException e) {
//            throw new DaoException(e);
//        }
    }

    @Override
    public Optional<User> getByEmail(String email) {
        Optional<User> userOptional;
        try (Connection connection = connectionPool.takeConnection()) {
            //noinspection unchecked
            userOptional = executeQuery(connection,
                    "SELECT * FROM Users WHERE email=?",
                    userBuilderFromRS,
                    email)
                    .stream()
                    .findFirst();
        } catch (SQLException e) {
            throw new DaoException(e);
        }
        return userOptional;
    }

    @Override
    public List<User> getByName(String name, Integer offset, Integer limit) {
        List<User> list;
        try (Connection connection = connectionPool.takeConnection()) {
            //noinspection unchecked
            list = executeQuery(connection,
                    "(SELECT * FROM Users WHERE first_name = ? UNION SELECT * FROM Users WHERE last_name = ?) LIMIT ? OFFSET ?",
                    userBuilderFromRS,
                    name, name, limit, offset);
        } catch (SQLException e) {
            throw new DaoException(e);
        }
        return list;
    }

    @Override
    public List<User> getByNames(String firstName, String lastName, Integer offset, Integer limit) {
        List<User> list;
        try (Connection connection = connectionPool.takeConnection()) {
            //noinspection unchecked
            list = executeQuery(connection,
                    "(SELECT * FROM Users WHERE first_name = ? AND last_name=? " +
                            "UNION SELECT * FROM Users WHERE first_name = ? AND last_name=?) LIMIT ? OFFSET ?",
                    userBuilderFromRS,
                    firstName, lastName, lastName, firstName, limit, offset);
        } catch (SQLException e) {
            throw new DaoException(e);
        }
        return list;
    }

    @Override
    public List<User> getFriends(Long id, Integer offset, Integer limit) {
        List<User> list;
        try (Connection connection = connectionPool.takeConnection()) {
            //noinspection unchecked
            list = executeQuery(connection,
                    "SELECT * FROM Users WHERE id= (SELECT recipient r_id FROM Relations WHERE sender=? AND type=3 UNION " +
                            "SELECT sender r_id FROM Relations WHERE recipient=? AND type=3) LIMIT ? OFFSET ?",
                    userBuilderFromRS,
                    id, id, limit, offset);
        } catch (SQLException e) {
            throw new DaoException(e);
        }
        return list;
    }
}