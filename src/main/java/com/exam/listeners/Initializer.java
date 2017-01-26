package com.exam.listeners;


import com.exam.connection_pool.ConnectionPool;
import com.exam.connection_pool.ConnectionPoolException;
import com.exam.dao.h2.UserDAOImpl;
import com.exam.dao.UserDAO;
import com.exam.logic.services.UserService;
import com.exam.util.DataScriptExecutor;
import lombok.extern.log4j.Log4j;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@Log4j
@WebListener
public class Initializer implements ServletContextListener {
    public static final String USER_DAO = "userDao";
    public static final String PROFILE_DAO = "profileDao";
    public static final String RELATION_DAO = "relationDao";
    public static final String MESSAGE_DAO = "messageDao";
    public static final String DIALOG_DAO = "dialogDao";
    public static final String USER_SERVICE = "userService";

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        ServletContext context = sce.getServletContext();
        String pathToDbConfig = context.getRealPath("/") + "WEB-INF/classes/";
        ConnectionPool.create(pathToDbConfig + "db.properties");
        ConnectionPool connectionPool = ConnectionPool.getInstance();
        try {
            connectionPool.initPoolData();
            log.info("Connection pool successfully initialized");
        } catch (ConnectionPoolException e) {
            log.error("Connection pool initialization error ", e);
        }

        connectionPool.executeScript(pathToDbConfig + "H2Init.sql");
        connectionPool.executeScript(pathToDbConfig + "usersH2Init.sql");
        log.info("SQL initialization has done successfully");

        UserDAO userDAO = new UserDAOImpl(connectionPool);
        context.setAttribute(USER_DAO, userDAO);
        context.setAttribute(USER_SERVICE, new UserService(userDAO));
    }
}