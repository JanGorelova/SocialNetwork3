package com.exam.dao.h2;

import com.exam.connection_pool.ConnectionPool;
import com.exam.dao.*;

/**
 * Created by Vasiliy Bobkov on 27.03.2017.
 */
public class H2DaoFactory implements AbstractDaoFactory {
    @Override
    public UserDAO createUserDAO(ConnectionPool connectionPool) {
        return new UserDAOImpl(connectionPool);
    }

    @Override
    public ProfileDAO createProfileDAO(ConnectionPool connectionPool) {
        return new ProfileDAOImpl(connectionPool);
    }

    @Override
    public PhotoDAO createPhotoDAO(ConnectionPool connectionPool) {
        return new PhotoDAOImpl(connectionPool);
    }

    @Override
    public ChatDAO createChatDAO(ConnectionPool connectionPool) {
        return new ChatDAOImpl(connectionPool);
    }

    @Override
    public PostDAO createPostDAO(ConnectionPool connectionPool) {
        return new PostDAOImpl(connectionPool);
    }

    @Override
    public TeamDAO createTeamDAO(ConnectionPool connectionPool) {
        return new TeamDAOImpl(connectionPool);
    }

    @Override
    public RelationDAO createRelationDAO(ConnectionPool connectionPool) {
        return new RelationDAOImpl(connectionPool);
    }
}
