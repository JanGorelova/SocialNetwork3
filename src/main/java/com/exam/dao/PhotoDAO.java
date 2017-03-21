package com.exam.dao;

import com.exam.models.Photo;

import java.util.Optional;

public interface PhotoDAO extends BaseDAO<Photo,Long> {
    /**
     * Метод вовращает из БД аватарку пользователя
     * @param id идентификатор пользователя
     * @return Optional
     */
    Optional<Photo> getAvaByUserId(Long id);
}
