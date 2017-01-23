package com.exam.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

import java.io.Serializable;

@AllArgsConstructor
@Builder
@Value
public class User implements Serializable{
    private static final long serialVersionUID = -8951846441704736309L;
    public enum Role {
        USER,
        MODERATOR,
        ADMIN;
    }
    private final Long id;
    private final String email;
    private final String password;
    private final String firstName;
    private final String lastName;
    private final Integer gender;
    private final Integer role;
}