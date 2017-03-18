package com.exam.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

import java.time.Instant;

@Value
@Builder
@AllArgsConstructor
public class Post {
    Long id;
    Long sender;
    Long recipient;
    Instant time;
    String message;
}