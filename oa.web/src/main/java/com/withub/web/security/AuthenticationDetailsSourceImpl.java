package com.withub.web.security;

import com.withub.model.security.LoginInfo;

import javax.servlet.http.HttpServletRequest;

public class AuthenticationDetailsSourceImpl implements org.springframework.security.authentication.AuthenticationDetailsSource<HttpServletRequest, LoginInfo> {

    @Override
    public LoginInfo buildDetails(HttpServletRequest request) {

        LoginInfo loginInfo = new LoginInfo();
        loginInfo.setClientIdentityCode(request.getRemoteAddr());
        loginInfo.setLoginType("Web");
        loginInfo.setAccountName(request.getParameter("username"));
        loginInfo.setPassword(request.getParameter("password"));
        return loginInfo;
    }

}
