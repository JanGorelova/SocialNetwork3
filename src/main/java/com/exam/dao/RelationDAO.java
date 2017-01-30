package com.exam.dao;

import com.exam.models.Relation;

import java.util.List;

public interface RelationDAO extends BaseDAO<Relation, Long> {
    List<Long> getFriendsID(Long id, Integer offset, Integer limit);

    Relation getBetween(Long sender, Long recipient);

    void addFriend(Long sender, Long recipient);

    List<Long> getIncomingID(Long userID, int offset, int limit);

    List<Long> getRequestID(Long userID, int offset, int limit);
}