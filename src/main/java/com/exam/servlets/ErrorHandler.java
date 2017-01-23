package com.exam.servlets;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import static com.exam.logic.Constants.ERROR_MSG;
import static com.exam.servlets.ErrorHandler.ErrorCode.COMMON_ERROR;
import static com.exam.servlets.ErrorHandler.ErrorCode.ERROR_404;

public class ErrorHandler extends HttpServlet{
    @RequiredArgsConstructor
    public enum ErrorCode {
        ERROR_404("error.404"),
        REGISTRATION_FAIL("error.registrationFail"),
        LOGIN_FAIL("error.loginFail"),
        FRIENDS_SEARCH_FAIL("error.friends.search"),
        LOCALE_ERROR("error.locale"),
        COMMON_ERROR("error.common"),
        USER_NOT_FOUND("error.userNotFound"),
        NOT_AUTH("error.notAuth"),
        SELECT_FILE_ERROR("error.selectFile"),
        FILE_UPLOAD_ERROR("error.fileUpload"),
        EMAIL_ALREADY_EXIST("error.emailExist");

        @Getter
        private final String propertyName;
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        requestProcess(request, response);
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        requestProcess(request, response);
    }

    private void requestProcess(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String code = request.getParameter("code");
        String msg = request.getAttribute(ERROR_MSG) != null ? (String) request.getAttribute(ERROR_MSG) :
                code.equals("404") ? ERROR_404.getPropertyName() : COMMON_ERROR.getPropertyName();
        request.setAttribute(ERROR_MSG, msg);
        request.getRequestDispatcher("/error.jsp").forward(request, response);
    }
}