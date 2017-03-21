package com.exam.dao;

/**
 * Класс-обёртка для ошибкок, возникающих в DAO слое. Превращает checked в unchecked.
 */
public class DaoException extends RuntimeException {
    public DaoException(Exception e) {
        super(e);
    }

    public DaoException(String message) {
        super(message);
    }

    public DaoException(String message, Exception e) {
        super(message, e);
    }
}