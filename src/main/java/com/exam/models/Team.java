package com.exam.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

import java.io.Serializable;

@Value
@AllArgsConstructor
@Builder
public class Team implements Serializable{
    private static final long serialVersionUID = 5983787439448197866L;
    private final Long id;
    private final String name;
}