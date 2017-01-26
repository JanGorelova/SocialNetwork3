package com.exam.dao.h2;

import com.exam.connection_pool.ConnectionPool;
import com.exam.connection_pool.ConnectionPoolException;
import com.exam.dao.ProfileDAO;
import com.exam.dao.TeamDAO;
import com.exam.dao.UserDAO;
import com.exam.models.Profile;
import com.exam.models.Team;
import com.exam.models.User;
import com.exam.util.DataScriptExecutor;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Optional;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class TeamDAOImplTest {
    private static TeamDAO teamDAO;
    private static UserDAO userDAO;
    private static ProfileDAO profileDAO;

    @BeforeClass
    public static void DBinit() throws ConnectionPoolException {
        ConnectionPool.create("src/main/resources/db.properties");
        ConnectionPool connectionPool = ConnectionPool.getInstance();
        connectionPool.initPoolData();
        DataScriptExecutor.initSqlData("src/main/resources/H2Init.sql");
        teamDAO = new TeamDAOImpl(connectionPool);
        userDAO = new UserDAOImpl(connectionPool);
        profileDAO = new ProfileDAOImpl(connectionPool);
    }

    @Test
    public void create() throws Exception {
        Team team = Team.builder().name("Вихрь").build();
        teamDAO.create(team);
        assertTrue(teamDAO.getByName("Вихрь").isPresent());
    }

    @Test
    public void read() throws Exception {
        Team team = Team.builder().name("Ураган").build();
        teamDAO.create(team);
        assertTrue(teamDAO.getByName("Ураган").isPresent());
    }

    @Test
    public void update() throws Exception {
        Team team = Team.builder().name("Созвездие").build();
        teamDAO.create(team);
        teamDAO.getByName("Созвездие")
                .map(s -> new Team(s.getId(), "Сатурн"))
                .ifPresent(s -> teamDAO.update(s));
        assertTrue(teamDAO.getByName("Сатурн").isPresent());
    }

    @Test
    public void delete() throws Exception {
        String email = "delete@TeamDaoTest.org";
        String teamName = "The delete.teamDao";

        Team team = Team.builder().name(teamName).build();
        teamDAO.create(team);

        userDAO.create(User.builder()
                .email(email)
                .firstName(" ")
                .lastName(" ")
                .password(" asd")
                .role(User.ROLE_USER)
                .gender(User.GENDER_MALE)
                .build());
        profileDAO.create(Profile.builder().id(userDAO.getByEmail(email).get().getId()).team(teamName).build());

        Optional<Team> teamOptional = teamDAO.getByName(teamName);
        assertTrue(teamOptional.isPresent());
        teamOptional.ifPresent(s -> teamDAO.delete(s.getId()));
        teamOptional = teamDAO.getByName(teamName);
        assertFalse(teamOptional.isPresent());
    }
}