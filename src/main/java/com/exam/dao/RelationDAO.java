package com.exam.dao;

import com.exam.models.Relation;

import java.util.List;

public interface RelationDAO extends BaseDAO<Relation, Long> {
    /**
     * Метод вовращает список друзей, ограниченный параметрами
     */
    List<Long> getFriendsID(Long id, Integer offset, Integer limit);

    /**
     * Метод возвращает отношение междё двумя пользователями.
     * @param sender идентефикатор пользователя, от которого исходит отношение
     * @param recipient идентификатор пользователя, получаетль отношения
     * @return Relation
     */
    Relation getBetween(Long sender, Long recipient);

    /**
     * Добавление пользователя в друзья, если есть входящий запрос.
     * Если входящей заявки нет, то будет выброшено исключение.
     * @param sender идентификатор пользователя, который принимает заявку в друзья
     * @param recipient идентификат пользователя, от которого исходил запрос в друзья
     */
    void addFriend(Long sender, Long recipient);

    /**
     * Метод вовращает список идентификаторов пользователей, которые отправили заявку в друзья.
     */
    List<Long> getIncomingID(Long userID, int offset, int limit);

    /**
     * Метод вовращает список идентификаторов пользователей, которым пользователь отправил запрос на добавление в друзья.
     */
    List<Long> getRequestID(Long userID, int offset, int limit);
}