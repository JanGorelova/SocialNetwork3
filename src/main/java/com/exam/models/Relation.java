package com.exam.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

import java.io.Serializable;

@Value
@AllArgsConstructor
@Builder
public class Relation implements Serializable {
    private static final long serialVersionUID = -4708283852516612162L;
    private final Long id;
    private final Long sender;
    private final Long recipient;
    private final Integer type;
}