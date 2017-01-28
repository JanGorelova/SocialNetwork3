package com.exam.dao.h2;

import com.exam.connection_pool.ConnectionPool;
import com.exam.connection_pool.ConnectionPoolException;
import com.exam.dao.RelationDAO;
import com.exam.dao.UserDAO;
import com.exam.models.Relation;
import com.exam.models.User;
import org.junit.BeforeClass;
import org.junit.Test;

import static com.exam.logic.Constants.FRIEND;
import static com.exam.logic.Constants.REQUEST;
import static org.junit.Assert.assertTrue;

public class RelationDAOImplTest {
    private static UserDAO userDao;
    private static RelationDAO relationDAO;

    @BeforeClass
    public static void init() throws ConnectionPoolException {
        ConnectionPool.create("src/main/resources/db.properties");
        ConnectionPool connectionPool = ConnectionPool.getInstance();
        connectionPool.initPoolData();
        connectionPool.executeScript("src/main/resources/H2Init.sql");
        userDao = new UserDAOImpl(connectionPool);
        relationDAO = new RelationDAOImpl(connectionPool);
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
    public void getFriendsId() throws Exception {
        String email1 = "getFriends1@relationDao.ru";
        String email2 = "getFriends2@relationDao.ru";
        String email3 = "getFriends3@relationDao.ru";
        userDao.create(generateUser(email1));
        userDao.create(generateUser(email3));
        userDao.create(generateUser(email2));
        Long userID_1=userDao.getByEmail(email1).get().getId();
        Long userID_2=userDao.getByEmail(email2).get().getId();
        Long userID_3=userDao.getByEmail(email3).get().getId();
        Relation relation = Relation.builder()
                .sender(userID_1)
                .recipient(userID_2)
                .type(FRIEND)
                .build();
        relationDAO.create(relation);

        relation = Relation.builder()
                .sender(userID_1)
                .recipient(userID_3)
                .type(FRIEND)
                .build();
        relationDAO.create(relation);

        assertTrue(relationDAO.getFriendsID(userID_1).contains(userID_2));
        assertTrue(relationDAO.getFriendsID(userID_1).contains(userID_3));
    }

    @Test
    public void getBetween() throws Exception {

    }

    @Test
    public void create() throws Exception {

    }

    @Test
    public void read() throws Exception {

    }

    @Test
    public void update() throws Exception {

    }

    @Test
    public void delete() throws Exception {

    }

}