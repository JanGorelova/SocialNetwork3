package com.exam.logic;

import com.exam.logic.actions.LocaleAction;
import com.exam.logic.actions.LoginAction;

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
            return "/WEB-INF/jsp/login.jsp";
        });
        actions.put("GET/static/locale", new LocaleAction());
        actions.put("POST/j_security_check",new LoginAction());

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