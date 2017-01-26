package com.exam.dao.h2;

import com.exam.connection_pool.ConnectionPool;
import com.exam.dao.DaoException;
import com.exam.dao.ProfileDAO;
import com.exam.models.Profile;
import lombok.RequiredArgsConstructor;

import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.util.Optional;

@RequiredArgsConstructor
public class ProfileDAOImpl implements ProfileDAO {
    private final ConnectionPool connectionPool;

    @Override
    public void create(Profile profile) {
        try (Connection connection = connectionPool.takeConnection()) {
            executeUpdate(connection,
                    "INSERT INTO Profiles (id, telephone, birthday, country, city, university, team, " +
                            "position, about) VALUES(?,?,?,?,?,?, (SELECT t.id FROM Teams t WHERE t.name=?),?,?)",
                    profile.getId(),
                    profile.getTelephone(),
                    Optional.ofNullable(profile.getBirthday())
                            .map(Date::valueOf)
                            .orElse(null),
                    profile.getCountry(),
                    profile.getCity(),
                    profile.getUniversity(),
                    profile.getTeam(),
                    profile.getPosition(),
                    profile.getAbout());
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public Optional<Profile> read(Long key) {
        Optional<Profile> profileOptional;
        try (Connection connection = connectionPool.takeConnection()) {
            //SELECT i.id, i.model_id, g.name, g.caliber FROM Instance i, Gun g WHERE i.model_id = g.id
            profileOptional = executeQuery(connection,
                    "SELECT p.*, t.name team FROM (SELECT * FROM Profiles WHERE id=?) AS p " +
                            "JOIN Teams AS t ON t.id = p.id;",
//                    "SELECT * FROM (SELECT id, telephone, birthday, country, city, university, team," +
//                            " position, about  FROM Profiles p WHERE u.id=? UNION SELECT t.team_name FROM Teams t" +
//                            " WHERE t.id=p.team)",
                    (rs) -> Profile.builder()
                            .id(rs.getLong("id"))
                            .telephone(rs.getString("telephone"))
                            .birthday(Optional.ofNullable(rs.getDate("birthday"))
                                    .map(Date::toLocalDate)
                                    .orElse(null))
                            .country(rs.getString("country"))
                            .city(rs.getString("city"))
                            .university(rs.getString("university"))
                            .team(rs.getString("name"))
                            .position(rs.getString("position"))
                            .about(rs.getString("about"))
                            .build(),
                    key)
                    .stream()
                    .findFirst();
        } catch (SQLException e) {
            throw new DaoException(e);
        }
        return profileOptional;
    }

    @Override
    public void update(Profile profile) {
        try (Connection connection = connectionPool.takeConnection()) {
            executeUpdate(connection,
                    "UPDATE Profiles SET telephone=?, birthday=?, country=?, city=?, university=?, " +
                            "team=(SELECT t.id FROM Teams t WHERE t.name=?), position=?, about=? WHERE id=?",
                    profile.getTelephone(),
                    Optional.ofNullable(profile.getBirthday())
                            .map(Date::valueOf)
                            .orElse(null),
                    profile.getCountry(),
                    profile.getCity(),
                    profile.getUniversity(),
                    profile.getTeam(),
                    profile.getPosition(),
                    profile.getAbout(),
                    profile.getId());
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public void delete(Long id) {
        try (Connection connection = connectionPool.takeConnection()) {
            executeUpdate(connection,
                    "DELETE FROM Profiles WHERE id=?",
                    id);
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }
}