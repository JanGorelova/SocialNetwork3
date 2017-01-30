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
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@Log4j
@WebFilter(urlPatterns = {"/*"})
public class SecurityFilter extends HttpFilter {
    private static final String CURRENT_USER = "currentUser";
    private Matcher staticMatcher = Pattern.compile("^\\/static\\/.*").matcher("");
    private Matcher securityMatcher = Pattern.compile("\\/j_security_check$").matcher("");
    private Matcher notAuthMatcher = Pattern.compile("^\\/not_auth\\/.*").matcher("");

    @Override
    public void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        String path = Optional.ofNullable(request.getRequestURI()).orElse("");
        //log.debug("Path: " + path);
        if (!staticMatcher.reset(path).find()
                && !securityMatcher.reset(path).find()
                && !notAuthMatcher.reset(path).find()) {
            HttpSession session = request.getSession(true);
            if (session.getAttribute(CURRENT_USER) != null)
                chain.doFilter(request, response);
            else {
                request.getRequestDispatcher("/WEB-INF/jsp/not_auth/login.jsp").forward(request, response);
            }
        } else chain.doFilter(request, response);
    }
}