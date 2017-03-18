package com.exam.servlets;

import com.exam.models.User;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

import static com.exam.logic.Constants.CURRENT_USER;
import static com.exam.logic.Constants.ERROR_MSG;

@Log4j
@WebServlet("/error")
public class ErrorHandler extends HttpServlet {

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
//        String code = request.getParameter("code");
//        String msg = request.getAttribute(ERROR_MSG) != null ? (String) request.getAttribute(ERROR_MSG) :
//                code.equals("404") ? ERROR_404.getPropertyName() : COMMON_ERROR.getPropertyName();
//        request.setAttribute(ERROR_MSG, msg);
        Throwable throwable = (Throwable) request
                .getAttribute("javax.servlet.error.exception");
        log.error(throwable);
        Integer statusCode = (Integer) request
                .getAttribute("javax.servlet.error.status_code");
        String servletName = (String) request
                .getAttribute("javax.servlet.error.servlet_name");
        if (servletName == null) {
            servletName = "Unknown";
        }
        String requestUri = (String) request
                .getAttribute("javax.servlet.error.request_uri");
        if (requestUri == null) {
            requestUri = "Unknown";
        }
        request.setAttribute("statusCode", statusCode);
        request.setAttribute("servletName", servletName);
        request.setAttribute("requestUri", requestUri);

        User currentUser = (User) request.getSession().getAttribute(CURRENT_USER);
        String errorMsg = null;
        if (currentUser.getRole() == User.ROLE_ADMIN) {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw, true);
            throwable.printStackTrace(pw);
            errorMsg = sw.getBuffer().toString();
            request.setAttribute(ERROR_MSG,errorMsg);
        }

        request.getRequestDispatcher("/WEB-INF/jsp/error.jsp").forward(request, response);
    }
}