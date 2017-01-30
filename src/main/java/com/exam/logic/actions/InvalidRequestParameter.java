package com.exam.logic.actions;

public class InvalidRequestParameter extends RuntimeException {
    public InvalidRequestParameter(Exception e) {
        super(e);
    }

    public InvalidRequestParameter(String message) {
        super(message);
    }

    public InvalidRequestParameter(String message, Exception e) {
        super(message, e);
    }
}
