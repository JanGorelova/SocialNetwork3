package com.exam.logic.actions;

import com.exam.logic.Action;
import lombok.SneakyThrows;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class LocaleAction implements Action {
    @SneakyThrows
    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession();
        session.setAttribute("language", request.getParameter("language"));
        String referer = request.getHeader("referer");
        response.sendRedirect(referer != null ? referer : request.getContextPath());
        return null;
    }
}