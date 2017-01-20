package com.exam.services;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@FunctionalInterface
public interface Action {
    String execute(HttpServletRequest request, HttpServletResponse response);
}