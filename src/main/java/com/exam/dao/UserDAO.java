package com.exam.dao;

import com.exam.models.User;

import java.util.Optional;

public interface UserDAO extends BaseDAO<User, Long> {
    Optional<User> getByEmail(String email);
}