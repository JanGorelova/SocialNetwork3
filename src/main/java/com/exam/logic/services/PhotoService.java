package com.exam.logic.services;


import com.exam.dao.PhotoDAO;
import com.exam.models.Photo;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

@RequiredArgsConstructor
public class PhotoService {
    private final PhotoDAO photoDAO;

    public void savePhoto(Photo ava) {
        photoDAO.create(ava);
    }

    public Optional<Photo> getUserAva(Long id) {
        return photoDAO.getAvaByUserId(id);
    }
}