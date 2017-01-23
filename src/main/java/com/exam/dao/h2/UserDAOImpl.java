package com.exam.dao.h2;

import com.exam.connection_pool.ConnectionPool;
import com.exam.dao.DaoException;
import com.exam.dao.ResultSetProcessor;
import com.exam.dao.UserDAO;
import com.exam.models.User;
import lombok.RequiredArgsConstructor;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Optional;

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
    public Optional<User> getByID(Long id) {
        Optional<User> userOptional;
        try (Connection connection = connectionPool.takeConnection()) {
            userOptional = executeQuery(connection,
                    "SELECT * FROM Users WHERE id=?",
                    userBuilderFromRS,
                    id)
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
        try (Connection connection = connectionPool.takeConnection()) {
            executeUpdate(connection,
                    "DELETE FROM Users WHERE id=?",
                    id);
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public Optional<User> getByEmail(String email) {
        Optional<User> userOptional;
        try (Connection connection = connectionPool.takeConnection()) {
            userOptional = executeQuery(connection,
                    "SELECT * FROM Users WHERE email=?",
                    userBuilderFromRS,
                    email)
                    .findFirst();
        } catch (SQLException e) {
            throw new DaoException(e);
        }
        return userOptional;
    }
}