package com.exam.dao.h2;

import com.exam.connection_pool.ConnectionPool;
import com.exam.connection_pool.ConnectionPoolException;
import com.exam.dao.UserDAO;
import com.exam.models.User;
import com.exam.util.DataScriptExecutor;
import lombok.SneakyThrows;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Optional;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class UserDAOImplTest {
    private static UserDAO userDao;

    @BeforeClass
    public static void DBinit() throws ConnectionPoolException {
        ConnectionPool.create("src/main/resources/db.properties");
        ConnectionPool connectionPool = ConnectionPool.getInstance();
        connectionPool.initPoolData();
        DataScriptExecutor.initSqlData("src/main/resources/H2Init.sql");
        userDao = new UserDAOImpl(connectionPool);
    }

    @Test
    @SneakyThrows
    public void getById() throws Exception {
        assertTrue(userDao.getByID(1L).isPresent());
    }

    @Test
    @SneakyThrows
    public void getByEmail() throws Exception {
        userDao.create(new User(0L, "test@junit.ru", "123456", "Иван", "Иванов", 1, User.Role.ADMIN.ordinal()));
        assertTrue(userDao.getByEmail("test@junit.ru").isPresent());
    }

//    @Test
//    @SneakyThrows
//    public void getByNames() throws Exception {
//
//        assertTrue(userDao.getByNames("Василий", "Бобков").size() > 0);
//    }

    @Test
    @SneakyThrows
    public void create() throws Exception {

        userDao.create(new User(0L, "vasya@ya.ru", "123456", "Иван", "Иванов", 1, User.Role.ADMIN.ordinal()));
        assertTrue(userDao.getByEmail("vasya@ya.ru").isPresent());
    }

    @Test
    @SneakyThrows
    public void update() throws Exception {
        userDao.create(new User(0L, "oldmail@exam.com", "123456", "Иван", "Иванов", 1, User.Role.ADMIN.ordinal()));
        Optional<User> userOptional = userDao.getByEmail("oldmail@exam.com");
        userOptional.map(user1 ->
                new User(
                        user1.getId(),
                        "newmail@exam.com",
                        user1.getPassword(),
                        user1.getFirstName(),
                        user1.getLastName(),
                        user1.getGender(),
                        user1.getRole()
                )).ifPresent(user2 -> userDao.update(user2));
        assertTrue(userDao.getByEmail("newmail@exam.com")
                .orElseThrow(()->new RuntimeException("User 2 not found!"))
                .getEmail()
                .equals("newmail@exam.com"));
    }

    @Test
    @SneakyThrows
    public void delete() throws Exception {

        userDao.create(new User(0L, "example3@ya.ru", "123456", "Иван", "Иванов", 1, User.Role.ADMIN.ordinal()));
        Optional<User> userOptional = userDao.getByEmail("example3@ya.ru");
        assertTrue(userOptional.isPresent());
        long user_id = userOptional.get().getId();
        userDao.delete(user_id);
        assertFalse(userDao.getByEmail("example3@ya.ru").isPresent());
    }
}