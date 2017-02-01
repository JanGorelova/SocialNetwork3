package com.exam.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.List;

@Value
@AllArgsConstructor
@Builder
public class Chat implements Serializable {

    private static final long serialVersionUID = 5436815518880496441L;
    private final Long id;
    private final Long creatorID;
    private final String name;
    private final String description;
    private final ZonedDateTime lastUpdate;
    private final ZonedDateTime startTime;
    private final ZonedDateTime lastRead;
    private final List<Long> participantsID;
}