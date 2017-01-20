package com.exam.filters;

import lombok.extern.log4j.Log4j;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;



@Log4j
@WebFilter
public class SecurityFilter extends HttpFilter {
    private static final String CURRENT_USER="currentUser";
    @Override
    public void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpSession session = request.getSession(true);
        if (session.getAttribute(CURRENT_USER) != null)
            chain.doFilter(request, response);
        else {
            request.getRequestDispatcher("/index.jsp").forward(request, response);
        }
    }
}