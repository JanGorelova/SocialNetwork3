package com.exam.util;

import java.util.function.Consumer;
import java.util.function.Function;

@FunctionalInterface
public interface ExceptionalConsumer<T, E extends Throwable> extends Consumer<T> {

    void call(T t) throws E;

    @Override
    default void accept(T t) {
        try {
            call(t);
        } catch (Throwable e) {
            //noinspection unchecked
            ifThrowable((E) e);
        }
    }

    default void ifThrowable(E e) {
        throw new RuntimeException(e);
    }

    static <T, E extends Throwable> Consumer<T> toUncheckedConsumer(ExceptionalConsumer<T, E> exceptionalConsumer) {
        return exceptionalConsumer;
    }
}