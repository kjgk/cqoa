package com.withub.web.servlet;

import com.withub.common.util.StringUtil;
import com.withub.web.common.Constants;
import org.apache.commons.lang.StringUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class SecurityServlet extends HttpServlet {

    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String action = request.getRequestURI().replaceAll(request.getServletPath(), "").replaceAll("/", "");

        if (StringUtils.equalsIgnoreCase(action, Constants.SESSION_INVALID)) {
            if (StringUtil.compareValue(request.getHeader("x-requested-with"), "XMLHttpRequest")) {
                response.getWriter().write(Constants.SESSION_INVALID);
            } else {
                response.sendRedirect(request.getContextPath() + "/security/login.page");
            }
        } else if (StringUtils.equalsIgnoreCase(action, Constants.SESSION_EXPIRED)) {
            if (StringUtil.compareValue(request.getHeader("x-requested-with"), "XMLHttpRequest")) {
                response.getWriter().write(Constants.SESSION_EXPIRED);
            } else {
                response.sendRedirect(request.getContextPath() + "/security/login.page");
            }
        } else {
            if (StringUtil.compareValue(request.getHeader("x-requested-with"), "XMLHttpRequest")) {
                response.getWriter().write(Constants.SESSION_INVALID);
            } else {
                response.sendRedirect(request.getContextPath() + "/security/login.page");
            }
        }


    }
}
