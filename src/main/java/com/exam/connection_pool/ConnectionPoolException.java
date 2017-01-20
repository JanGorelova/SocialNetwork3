package com.exam.connection_pool;

public class ConnectionPoolException extends RuntimeException {
    public ConnectionPoolException(String message, Exception e){
        super(message, e);
    }
}