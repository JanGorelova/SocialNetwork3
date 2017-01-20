package com.exam.servlets;

import com.exam.services.AbstractActionFactory;
import com.exam.services.Action;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(urlPatterns = {"/data/*","/locale","/logout"})
public class FrontController extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    private void processRequest(HttpServletRequest request,
                                HttpServletResponse response)
            throws ServletException, IOException {
        Action action =
                AbstractActionFactory
                        .getInstance()
                        .getAction(request);
        String view = action.execute(request, response);
        if (view != null)
            getServletContext().getRequestDispatcher(view).forward(request, response);
    }
}