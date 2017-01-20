package com.exam.services;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Vasiliy Bobkov on 19.01.2017.
 */
public class ActionFactory {
    private Map<String, Action> actions = new ConcurrentHashMap<>();

    public ActionFactory() {
        actions.put("GET/data/my", (r, s) -> "/jsp/data/my.jsp");
        actions.put("POST/logout", (r, s) -> {
            r.getSession().invalidate();
            return "/index.jsp";
        });
        //actions.put()

    }

    public Action getAction(HttpServletRequest request) {
        String path = request.getServletPath() + request.getPathInfo();
        String actionKey = request.getMethod() + path;
        System.out.println("New request: " + actionKey);
        return Optional
                .ofNullable(actions.get(actionKey))
                .orElse((r, s) -> "/index.jsp");
    }
}