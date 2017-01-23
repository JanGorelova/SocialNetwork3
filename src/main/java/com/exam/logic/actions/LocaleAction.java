package com.exam.logic.actions;

import com.exam.logic.Action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class LocaleAction implements Action {

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession();
        session.setAttribute("language", request.getParameter("language"));
        session.setAttribute("successMsg","success.change.local");
        return "/WEB-INF/jsp/success.jsp";
    }
}