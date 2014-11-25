package com.withub.web.servlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class DispatcherServlet extends HttpServlet {

    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String value = req.getRequestURI().replaceFirst(req.getContextPath(), "");
        if ("/home".equals(value)) {
            req.getRequestDispatcher("/home.html").forward(req, resp);
        }
        if ("/flowTypeDiagram".equals(value)) {
            req.getRequestDispatcher("/diagram.html").forward(req, resp);
        }
    }
}
