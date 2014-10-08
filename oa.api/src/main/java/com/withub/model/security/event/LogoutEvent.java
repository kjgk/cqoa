package com.withub.model.security.event;

import org.springframework.context.ApplicationEvent;

/**
 * 定义退出事件
 */
public class LogoutEvent extends ApplicationEvent {

    private static final long serialVersionUID = 1L;

    private String accountId;

    public LogoutEvent(Object source, String accountId) {
        super(source);
        this.accountId = accountId;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

}
