package com.exam.dao;

import com.exam.models.User;

import java.util.List;
import java.util.Optional;

public interface UserDAO extends BaseDAO<User, Long> {
    /**
     * Метод вовращает пользователя, найденого в БД по email
     * @return Optional
     */
    Optional<User> getByEmail(String email);

    List<User> getByName(String name, Integer offset, Integer limit);

    List<User> getByNames(String firstName, String lastName, Integer offset, Integer limit);
}