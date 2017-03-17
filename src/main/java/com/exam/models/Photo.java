package com.exam.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

import java.io.Serializable;
import java.time.Instant;
import java.util.regex.Pattern;

@Value
@AllArgsConstructor
@Builder
public class Photo implements Serializable {
    private static final long serialVersionUID = 3636023724001183642L;
    private static final Pattern pattern = Pattern.compile(".jpg$");
    private final long id;
    private final long sender;
    private final Instant time;
    private final boolean avatar;
    private final String link;

    public String getLinkOfMinPhoto() {
        return pattern.matcher(link).replaceFirst("_min.jpg");
    }
}