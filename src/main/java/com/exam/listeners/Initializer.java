package com.exam.listeners;


import com.exam.connection_pool.ConnectionPool;
import com.exam.connection_pool.ConnectionPoolException;
import com.exam.dao.ChatDAO;
import com.exam.dao.ProfileDAO;
import com.exam.dao.RelationDAO;
import com.exam.dao.UserDAO;
import com.exam.dao.h2.ChatDAOImpl;
import com.exam.dao.h2.ProfileDAOImpl;
import com.exam.dao.h2.RelationDAOImpl;
import com.exam.dao.h2.UserDAOImpl;
import com.exam.logic.services.ChatService;
import com.exam.logic.services.ProfileService;
import com.exam.logic.services.UserService;
import lombok.extern.log4j.Log4j;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import static com.exam.logic.Constants.*;

@Log4j
@WebListener
public class Initializer implements ServletContextListener {


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
        RelationDAO relationDAO = new RelationDAOImpl(connectionPool);
        context.setAttribute(USER_DAO, userDAO);
        context.setAttribute(RELATION_DAO, relationDAO);
        context.setAttribute(USER_SERVICE, new UserService(userDAO, relationDAO));

        ProfileDAO profileDAO = new ProfileDAOImpl(connectionPool);
        context.setAttribute(PROFILE_DAO, profileDAO);
        context.setAttribute(PROFILE_SERVICE, new ProfileService(profileDAO));

        ChatDAO chatDAO = new ChatDAOImpl(connectionPool);
        context.setAttribute(CHAT_DAO, profileDAO);
        context.setAttribute(CHAT_SERVICE, new ChatService(chatDAO));
    }
}