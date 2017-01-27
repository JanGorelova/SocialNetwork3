package com.exam.util;

import lombok.extern.log4j.Log4j;

import java.util.stream.Collectors;
import java.util.stream.Stream;
@Log4j
public abstract class NameNormalizer {
    public static String normalize(String name) {

        return Stream.of(name.split("-"))
                .filter(s->s.length()>0)
                .map(String::toLowerCase)
                .map(StringBuilder::new)
                .peek(sb -> sb.setCharAt(0, Character.toUpperCase(sb.charAt(0))))//Меняем регистр первого символа
                .collect(Collectors.joining("-"));
    }

    public static String multiNormalize(String name) {
        return Stream.of(name.split(" "))
                .map(NameNormalizer::normalize)
                .collect(Collectors.joining(" "));
    }
}