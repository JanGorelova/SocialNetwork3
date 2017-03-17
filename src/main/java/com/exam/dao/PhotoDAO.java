package com.exam.dao;

import com.exam.models.Photo;

import java.util.Optional;

public interface PhotoDAO extends BaseDAO<Photo,Long> {
    Optional<Photo> getAvaByUserId(Long id);
}
