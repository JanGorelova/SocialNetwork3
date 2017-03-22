package com.exam.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public interface BaseDAO<E, K> {
    void create(E entity);

    /**
     * Метод возвращает объект из базы данных, найденный по первичному ключу
     * @param key первичный ключ
     * @return Optional, который содержит объект в случае его существования в БД
     */
    Optional<E> read(K key);

    /**
     * Обновление состояния сущности в БД
     * @param entity сущность
     */
    void update(E entity);

    /**
     * Метод для удаления записи в БД по первичному ключу
     * @param id первичный ключ
     */
    void delete(K id);

    /**
     * Метод для запросов в базу данных
     * @param sql текст запроса
     * @param processor обработчик результатов
     * @param params параметры запроса
     * @param <T> объект запроса
     * @return обобщённый объект запроса
     */
    default <T> List<T> executeQuery(Connection connection,
                                   String sql,
                                   ResultSetProcessor<T> processor,
                                   Object... params) {
        List<T> list = new ArrayList<>();
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            int cnt = 0;
            for (Object param : params) {
                ps.setObject(++cnt, param);
            }
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(processor.execute(rs));
                }
            }
        } catch (SQLException e) {
            throw new DaoException(e);
        }
        return list;
    }

    /**
     * Метод для обновления и удаления данных.
     * @param sql текст запроса
     * @param params параметры, передаваемые в запросе
     */
    default void executeUpdate(Connection connection,
                               String sql,
                               Object... params) {
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            int count = 0;
            for (Object param : params) {
                ps.setObject(++count, param);
            }
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }
}