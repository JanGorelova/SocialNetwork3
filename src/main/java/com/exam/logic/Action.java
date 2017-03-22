package com.exam.logic;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

@FunctionalInterface
public interface Action {
    /**
     * Метод обработки запроса. Также внутри может быть отправлен ответ, при этом вовращается null.
     * @return отноительный путь страницы отображения. null, если ответ отправлен внутри метода.
     */
    String execute(HttpServletRequest request, HttpServletResponse response);
}