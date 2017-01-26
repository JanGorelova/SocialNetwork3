package com.exam.logic;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

@FunctionalInterface
public interface Action {

    String execute(HttpServletRequest request, HttpServletResponse response);
}