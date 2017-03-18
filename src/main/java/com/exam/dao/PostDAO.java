package com.exam.dao;

import com.exam.models.Post;

import java.util.List;

public interface PostDAO extends BaseDAO<Post, Long> {
    List<Post> getByRecipient(Long id, int offset, int limit);

    long getRows(Long id);
}