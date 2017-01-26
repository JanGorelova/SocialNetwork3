package com.exam.logic.services;

import com.exam.dao.ProfileDAO;
import com.exam.models.Profile;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ProfileService {
    private final ProfileDAO profileDAO;

    public Profile getById(Long id) {
        return profileDAO.read(id)
                .orElse(Profile.builder().id(id).build());
    }
}