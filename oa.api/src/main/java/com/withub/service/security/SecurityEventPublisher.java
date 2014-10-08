package com.withub.service.security;

import com.withub.model.security.LoginInfo;
import com.withub.model.security.event.LoginEvent;
import com.withub.model.security.event.LogoutEvent;
import com.withub.spring.SpringContextUtil;

/**
 * 定义安全事件发布器
 */
public final class SecurityEventPublisher {

    /**
     * 发布登录事件
     *
     * @param loginInfo 登录信息
     */
    public static void publishLoginEvent(Object source, LoginInfo loginInfo) {

        LoginEvent event = new LoginEvent(source, loginInfo);
        SpringContextUtil.getInstance().getApplicationContext().publishEvent(event);
    }

    /**
     * 发布退出事件
     *
     * @param accountId 帐号ID
     */
    public static void publishLogoutEvent(Object source, String accountId) {

        LogoutEvent event = new LogoutEvent(source, accountId);
        SpringContextUtil.getInstance().getApplicationContext().publishEvent(event);
    }
}
