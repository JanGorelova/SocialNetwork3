package com.exam.logic;

import com.exam.logic.actions.LocaleAction;
import com.exam.logic.actions.LoginAction;
import com.exam.logic.actions.ProfileAction;
import com.exam.logic.actions.RegistrationAction;

import javax.servlet.Registration;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class ActionFactory {
    private Map<String, Action> actions = new ConcurrentHashMap<>();
    public ActionFactory() {
        actions.put("GET/data/my", (r, s) -> "/jsp/data/my.jsp");
        actions.put("GET/logout", (r, s) -> {
            r.getSession().invalidate();
            return "/WEB-INF/jsp/not_auth/login.jsp";
        });
        actions.put("GET/not_auth/locale", new LocaleAction());
        actions.put("POST/j_security_check",new LoginAction());
        actions.put("GET/not_auth/registration",(r,s)->"/WEB-INF/jsp/not_auth/registration.jsp");
        actions.put("POST/not_auth/registration", new RegistrationAction());
        actions.put("GET/profile", new ProfileAction());

    }

    public Action getAction(HttpServletRequest request) {
        StringBuilder actionKey = new StringBuilder(request.getMethod());
        actionKey.append(request.getServletPath());
        if (request.getPathInfo() != null) actionKey.append(request.getPathInfo());

        System.out.println("New request: " + actionKey.toString());
        return Optional
                .ofNullable(actions.get(actionKey.toString()))
                .orElse((r, s) -> "/WEB-INF/jsp/404.jsp");
    }
}