package com.exam.dao;

import com.exam.connection_pool.ConnectionPool;

/**
 * Created by Vasiliy Bobkov on 27.03.2017.
 */
public interface AbstractDaoFactory {
    UserDAO createUserDAO(ConnectionPool connectionPool);

    ProfileDAO createProfileDAO(ConnectionPool connectionPool);

    PhotoDAO createPhotoDAO(ConnectionPool connectionPool);

    ChatDAO createChatDAO(ConnectionPool connectionPool);

    TeamDAO createTeamDAO(ConnectionPool connectionPool);

    PostDAO createPostDAO(ConnectionPool connectionPool);

    RelationDAO createRelationDAO(ConnectionPool connectionPool);
}
