package com.exam.dao;

import com.exam.models.Post;

import java.util.List;

public interface PostDAO extends BaseDAO<Post, Long> {
    /**
     * Метод вовращает список постов на стене пользователя, ограниченный параметрами.
     * @param id идентификатор пользователя
     * @param offset начало отсчёта в списке результатов
     * @param limit ограничение записей
     * @return List of Posts
     */
    List<Post> getByRecipient(Long id, int offset, int limit);

    /**
     * Вовращает количество постов на стене пользователя.
     * @param id идентефикатор пользователя
     * @return количество постов
     */
    long getRows(Long id);
}