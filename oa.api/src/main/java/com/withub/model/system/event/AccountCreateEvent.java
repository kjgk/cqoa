package com.withub.model.system.event;

import org.springframework.context.ApplicationEvent;

public class AccountCreateEvent extends ApplicationEvent {

    private AccountEventArgs accountEventArgs;

    public AccountCreateEvent(Object source, AccountEventArgs accountEventArgs) {
        super(source);
        this.accountEventArgs = accountEventArgs;
    }

    public AccountEventArgs getAccountEventArgs() {
        return accountEventArgs;
    }

    public void setAccountEventArgs(AccountEventArgs accountEventArgs) {
        this.accountEventArgs = accountEventArgs;
    }
}
