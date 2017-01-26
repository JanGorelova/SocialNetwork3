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

import static junit.framework.TestCase.assertFalse;
import static org.junit.Assert.assertTrue;

public class UserDAOImplTest {
    private static UserDAO userDao;
    private static ProfileDAO profileDAO;
    private static TeamDAO teamDAO;

    @BeforeClass
    public static void DBinit() throws ConnectionPoolException {
        ConnectionPool.create("src/main/resources/db.properties");
        ConnectionPool connectionPool = ConnectionPool.getInstance();
        connectionPool.initPoolData();
        DataScriptExecutor.initSqlData("src/main/resources/H2Init.sql",connectionPool);
        userDao = new UserDAOImpl(connectionPool);
        teamDAO = new TeamDAOImpl(connectionPool);
        profileDAO = new ProfileDAOImpl(connectionPool);
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
    public void read() throws Exception {
        final String email = "read@ya.ru";
        userDao.create(generateUser(email));
        assertTrue(userDao.getByEmail(email)
                .map(u -> userDao.read(u.getId()))
                .isPresent());
    }

    @Test
    public void getByEmail() throws Exception {
        final String email = "getByEmail@junit.org";
        userDao.create(generateUser(email));
        assertTrue(userDao.getByEmail(email).isPresent());
    }

    @Test
    public void create() throws Exception {
        final String email = "create@junit.org";
        userDao.create(generateUser(email));
        assertTrue(userDao.getByEmail(email).isPresent());
    }

    @Test
    public void update() throws Exception {
        final String oldMail = "oldmail@exam.com";
        final String newMail = "newmail@exam.com";
        userDao.create(generateUser(oldMail));
        userDao.getByEmail(oldMail)
                .map(user1 ->
                        new User(
                                user1.getId(),
                                newMail,
                                user1.getPassword(),
                                user1.getFirstName(),
                                user1.getLastName(),
                                user1.getGender(),
                                user1.getRole()
                        )).ifPresent(user2 -> userDao.update(user2));
        assertTrue(userDao.getByEmail(newMail).isPresent());
    }

//    @Test
//    public void delete() throws Exception {
//        final String email = "delete@UserDaoTest.org";
//        final String team_name = "User delete test";
//        userDao.create(generateUser(email));
//        teamDAO.create(Team.builder().name(team_name).build());
//        profileDAO.create(Profile.builder()
//                .id(userDao.getByEmail(email).orElseThrow(RuntimeException::new).getId())
//                .team(team_name)
//                .build());
//
//        Optional<User> userOptional = userDao.getByEmail(email);
//        assertTrue(userOptional.isPresent());
//        userOptional.ifPresent(u -> userDao.delete(u.getId()));
//        assertFalse(userDao.getByEmail(email).isPresent());
//    }
}