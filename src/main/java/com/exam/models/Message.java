package com.exam.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

import java.io.Serializable;
import java.time.ZonedDateTime;

@Value
@AllArgsConstructor
@Builder
public class Message implements Serializable {

    private static final long serialVersionUID = 3179109666291410918L;
    private final Long id;
    private final Long chatID;
    private final Long senderID;
    private final String senderName;
    private final String text;
    private final ZonedDateTime sendingTime;
}