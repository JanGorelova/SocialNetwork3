package com.exam.dao.h2;

import com.exam.connection_pool.ConnectionPool;
import com.exam.dao.DaoException;
import com.exam.dao.TeamDAO;
import com.exam.models.Team;
import lombok.RequiredArgsConstructor;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Optional;

@RequiredArgsConstructor
public class TeamDAOImpl implements TeamDAO {
    private final ConnectionPool connectionPool;

    @Override
    public void create(Team entity) {
        try (Connection connection = connectionPool.takeConnection()) {
            executeUpdate(connection,
                    "INSERT INTO Teams (name) VALUES (?)",
                    entity.getName());
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public Optional<Team> read(Long key) {
        Optional<Team> teamOptional;
        try (Connection connection = connectionPool.takeConnection()) {
            //noinspection unchecked
            teamOptional = executeQuery(connection,
                    "SELECT * FROM Teams WHERE id=?",
                    rs -> Team.builder()
                            .id(rs.getLong("id"))
                            .name(rs.getString("name"))
                            .build(),
                    key)
                    .stream().findFirst();
        } catch (SQLException e) {
            throw new DaoException(e);
        }
        return teamOptional;
    }

    @Override
    public void update(Team entity) {
        try (Connection connection = connectionPool.takeConnection()) {
            executeUpdate(connection,
                    "UPDATE Teams SET name=? WHERE id=?",
                    entity.getName(),
                    entity.getId());
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public void delete(Long id) {
        try (Connection connection = connectionPool.takeConnection()) {
            connection.setAutoCommit(false);
            executeUpdate(connection,
                    "UPDATE Profiles SET team=NULL WHERE team=?;",
                    id);
            executeUpdate(connection,
                    "DELETE FROM Teams WHERE id=?;",
                    id);
            connection.commit();
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public Optional<Team> getByName(String name) {
        Optional<Team> teamOptional;
        try (Connection connection = connectionPool.takeConnection()) {
            //noinspection unchecked
            teamOptional = executeQuery(connection,
                    "SELECT * FROM Teams WHERE name=?",
                    rs -> Team.builder()
                            .id(rs.getLong("id"))
                            .name(rs.getString("name"))
                            .build(),
                    name)
                    .stream()
                    .findFirst();
        } catch (SQLException e) {
            throw new DaoException(e);
        }
        return teamOptional;
    }
}