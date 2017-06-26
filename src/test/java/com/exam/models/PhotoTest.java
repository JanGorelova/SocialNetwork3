package com.exam.models;

import org.junit.Test;

import static org.junit.Assert.assertTrue;

/**
 * Created by Vasiliy Bobkov on 16.03.2017.
 */
public class PhotoTest {
    @Test
    public void getLinkOfMinPhoto() throws Exception {
        Photo photo = Photo.builder().id(1).link("/123253141234.jpg").build();
        assertTrue(photo.getLinkOfMinPhoto().equals("/123253141234_min.jpg"));
    }
}