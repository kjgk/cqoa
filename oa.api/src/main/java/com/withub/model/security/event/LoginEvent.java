package com.withub.model.security.event;

import com.withub.model.security.LoginInfo;
import org.springframework.context.ApplicationEvent;

/**
 * 定义登录事件
 */
public class LoginEvent extends ApplicationEvent {

    private static final long serialVersionUID = 1L;

    private LoginInfo loginInfo;

    public LoginEvent(Object source, LoginInfo args) {

        super(source);
        loginInfo = args;
    }

    public LoginInfo getLoginInfo() {

        return loginInfo;
    }

    public void setLoginInfo(LoginInfo loginInfo) {

        this.loginInfo = loginInfo;
    }

}
