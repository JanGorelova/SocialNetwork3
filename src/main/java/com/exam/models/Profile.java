package com.exam.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Value;

import java.io.Serializable;
import java.time.LocalDate;

@Value
@AllArgsConstructor
@Builder
public class Profile implements Serializable{
    private static final long serialVersionUID = -6739473715366973675L;
    private final Long id;
    private final String telephone;
    private final LocalDate birthday;
    private final String country;
    private final String city;
    private final String university;
    private final String team;
    private final String position;
    private final String about;
}