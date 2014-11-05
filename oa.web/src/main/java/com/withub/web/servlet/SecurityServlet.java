package com.withub.web.servlet;

import com.withub.common.util.StringUtil;
import com.withub.web.common.Constants;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class SecurityServlet extends HttpServlet {

    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        if (StringUtil.compareValue(request.getHeader("x-requested-with"), "XMLHttpRequest")) {
            response.getWriter().write(Constants.SESSION_INVALID);
        } else if (request.getHeader("accept") != null && request.getHeader("accept").indexOf("application/json") != -1) {
            response.getWriter().write(Constants.SESSION_INVALID);
        } else {
            response.sendRedirect(request.getContextPath() + "/security/login.page");
        }
    }
}
