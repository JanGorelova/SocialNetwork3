package com.exam.dao;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Функциональный интерфейс для обработки результатов SQL запроса.
 * @param <E>
 */
@FunctionalInterface
public interface ResultSetProcessor<E> {
     E execute(ResultSet resultSet)
            throws SQLException;
}