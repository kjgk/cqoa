package com.withub.web.security;


import com.withub.service.security.SecurityEventPublisher;
import com.withub.service.security.WithubUserDetails;
import org.springframework.security.core.Authentication;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class LogoutSuccessHandlerImpl implements org.springframework.security.web.authentication.logout.LogoutSuccessHandler {

    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        if (authentication != null) {
            WithubUserDetails userDetails = (WithubUserDetails) authentication.getPrincipal();
            SecurityEventPublisher.publishLogoutEvent(this, userDetails.getAccountId());
            response.sendRedirect(request.getContextPath());
        } else {
            response.sendRedirect(request.getContextPath());
        }

    }

}
