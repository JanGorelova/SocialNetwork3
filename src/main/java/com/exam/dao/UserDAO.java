package com.exam.dao;

import com.exam.models.User;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public interface UserDAO extends BaseDAO<User, Long> {
    Optional<User> getByEmail(String email);
    List<User> getFriends(Long id, Integer offset, Integer limit);
}