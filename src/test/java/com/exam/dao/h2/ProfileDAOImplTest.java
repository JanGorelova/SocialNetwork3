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

import java.time.LocalDate;
import java.util.Optional;

import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertTrue;

public class ProfileDAOImplTest {
    private static ProfileDAO profileDAO;
    private static UserDAO userDAO;
    private static TeamDAO teamDAO;

    @BeforeClass
    public static void DBinit() throws ConnectionPoolException {
        ConnectionPool.create("src/main/resources/db.properties");
        ConnectionPool connectionPool = ConnectionPool.getInstance();
        connectionPool.initPoolData();
        DataScriptExecutor.initSqlData("src/main/resources/H2Init.sql");

        profileDAO = new ProfileDAOImpl(connectionPool);
        userDAO = new UserDAOImpl(connectionPool);
        teamDAO = new TeamDAOImpl(connectionPool);
    }

    private User generateUser(String email) {
        return User.builder()
                .email(email)
                .password("123456")
                .firstName("Иван")
                .lastName("Иванов")
                .gender(User.GENDER_MALE)
                .role(User.ROLE_ADMIN)
                .build();
    }

    @Test
    public void create() throws Exception {
        Optional<User> user = prepareUserAndTeam("create@ProfileDaoTest.org", "The A team");
        assertTrue(user.isPresent());
        Profile profile = Profile.builder()
                .id(user.get().getId())
                .telephone("+777777")
                .team("The A team")
                .birthday(LocalDate.of(1993, 12, 1))
                .build();
        profileDAO.create(profile);
        assertTrue(profileDAO.read(profile.getId()).isPresent());
    }

    private Optional<User> prepareUserAndTeam(String userEmail, String teamName) {
        String email = "create@ProfileDaoTest.org";
        String team_name = "The A team";
        userDAO.create(generateUser(userEmail));
        teamDAO.create(Team.builder().name(teamName).build());
        return userDAO.getByEmail(userEmail);
    }

    @Test
    public void read() throws Exception {

    }

    @Test
    public void update() throws Exception {
        Optional<User> user = prepareUserAndTeam("update@ProfileDaoTest.org", "The B team");
        assertTrue(user.isPresent());
        Profile profile = Profile.builder()
                .id(user.get().getId())
                .telephone("+777777")
                .birthday(LocalDate.of(1993, 12, 1))
                .build();
        profileDAO.create(profile);

        assertTrue(profileDAO.read(profile.getId())
                .flatMap(p -> {
                    profileDAO.update(Profile
                            .builder()
                            .id(p.getId())
                            .telephone("+555555")
                            .build());
                    return profileDAO.read(p.getId());
                })
                .filter(p -> p.getTelephone().equals("+555555"))
                .isPresent());
    }

    @Test
    public void delete() throws Exception {
        Optional<User> user = prepareUserAndTeam("delete@ProfileDaoTest.org", "The C team");
        assertTrue(user.isPresent());
        Profile profile = Profile.builder()
                .id(user.get().getId())
                .telephone("+777777")
                .birthday(LocalDate.of(1993, 12, 1))
                .build();
        profileDAO.create(profile);

        assertFalse(profileDAO.read(profile.getId())
                .flatMap(p -> {
                    profileDAO.delete(p.getId());
                    return profileDAO.read(p.getId());
                })
                .isPresent());
    }

}