package com.withub.web.filter;

import com.withub.service.security.WithubUserDetails;
import com.withub.web.common.Constants;
import net.sf.json.JSONSerializer;
import org.apache.commons.lang.StringUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class LoginFilter extends OncePerRequestFilter {


    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        boolean success = false;
        boolean firstLogin = false;
        String resultCode = "";

        Integer passwordInvalidCount = (Integer) session.getAttribute(Constants.PASSWORD_INVALID_COUNT_KEY);
        String captcha1 = (String) session.getAttribute(com.google.code.kaptcha.Constants.KAPTCHA_SESSION_KEY);
        String captcha2 = request.getParameter("captcha");

        if (passwordInvalidCount != null && passwordInvalidCount >= 3 && !StringUtils.equalsIgnoreCase(captcha1, captcha2)) {
            resultCode = Constants.CAPTCHA_INVALID;
        } else {

            session.removeAttribute(com.google.code.kaptcha.Constants.KAPTCHA_SESSION_KEY);

            boolean isAjax = "XMLHttpRequest".equals(request.getHeader("X-Requested-With"));

            if (isAjax) {
                filterChain.doFilter(request, response);

                Authentication authentication = (Authentication) request.getAttribute("authentication");
                if (authentication == null) {
                    AuthenticationException authenticationException = (AuthenticationException) request.getAttribute("authenticationException");
                    String message = authenticationException.getMessage();
                    if ("User account is locked".equals(message)) {
                        resultCode = Constants.ACCOUNT_LOCKED;
                    } else {
                        resultCode = Constants.PASSWORD_INVALID;
                        session.setAttribute(Constants.PASSWORD_INVALID_COUNT_KEY, passwordInvalidCount == null ? 1 : passwordInvalidCount + 1);
                    }
                } else {
                    WithubUserDetails userDetails = (WithubUserDetails) authentication.getPrincipal();
                    success = true;
                }
            } else {
                response.getWriter().write("error request-with");
                response.getWriter().flush();
                response.getWriter().close();
                return;
            }
        }

        Map result = new HashMap();
        result.put("success", success);
        result.put("resultCode", resultCode);
        result.put("firstLogin", firstLogin);
        response.getWriter().write(JSONSerializer.toJSON(result).toString());
        response.getWriter().flush();
        response.getWriter().close();
    }
}
