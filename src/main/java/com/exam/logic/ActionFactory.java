package com.exam.logic;

import com.exam.logic.actions.LocaleAction;
import com.exam.logic.actions.LoginAction;
import com.exam.logic.actions.RegistrationAction;
import com.exam.logic.actions.chats.ChatListAction;
import com.exam.logic.actions.chats.GetPrivateChatAction;
import com.exam.logic.actions.chats.MessageListAction;
import com.exam.logic.actions.chats.SendMessageAction;
import com.exam.logic.actions.friends.*;
import com.exam.logic.actions.post.NewPost;
import com.exam.logic.actions.post.PostCleaner;
import com.exam.logic.actions.profile.ProfileAction;
import com.exam.logic.actions.profile.ProfileEditGetAction;
import com.exam.logic.actions.profile.ProfileEditPostAction;
import com.exam.logic.actions.upload.Avatar;
import lombok.extern.log4j.Log4j;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@Log4j
public class ActionFactory {
    private Map<String, Action> actions = new HashMap<>();
    public static final ActionFactory INSTANCE = new ActionFactory();

    public ActionFactory() {
        actions.put("GET/data/my", (r, s) -> "/jsp/data/my.jsp");
        actions.put("GET/logout", (r, s) -> {
            r.getSession().invalidate();
            return "/WEB-INF/jsp/not_auth/login.jsp";
        });
        actions.put("GET/not_auth/locale", new LocaleAction());
        actions.put("POST/j_security_check", new LoginAction());
        actions.put("GET/not_auth/registration", (r, s) -> "/WEB-INF/jsp/not_auth/registration.jsp");
        actions.put("POST/not_auth/registration", new RegistrationAction());
        actions.put("GET/profile", new ProfileAction());
        actions.put("GET/profile/edit", new ProfileEditGetAction());
        actions.put("POST/profile/edit", new ProfileEditPostAction());
        actions.put("GET/friends", new FriendsListAction());
        actions.put("GET/friends/search", new SearchFriendsAction());
        actions.put("POST/friends/incoming/accept", new AcceptIncomingAction());
        actions.put("POST/friends/request/send", new SendRequestAction());
        actions.put("POST/friends/cancel", new CancelAction());
        actions.put("GET/friends/incoming", new GetIncomingAction());
        actions.put("GET/friends/request", new GetRequestAction());
        actions.put("GET/chat", new ChatListAction());
        actions.put("GET/chat/window", new MessageListAction());
        actions.put("POST/chat/message/new", new SendMessageAction());
        actions.put("GET/chat/private", new GetPrivateChatAction());
        actions.put("POST/upload/avatar", new Avatar());
        actions.put("POST/post/new", new NewPost());
        actions.put("POST/post/delete", new PostCleaner());
    }

    public Action getAction(HttpServletRequest request) {
        StringBuilder actionKey = new StringBuilder(request.getMethod());
        actionKey.append(request.getServletPath());
        if (request.getPathInfo() != null) actionKey.append(request.getPathInfo());

        log.debug("New request: " + actionKey.toString());
        Action action = actions.get(actionKey.toString());
        return action != null ? action : (r, s) -> "/WEB-INF/jsp/404.jsp";
//        return Optional
//                .ofNullable(actions.get(actionKey.toString()))
//                .orElse((r, s) -> "/WEB-INF/jsp/404.jsp");
    }
}