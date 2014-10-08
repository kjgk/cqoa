package com.withub.model.system.event;

import com.withub.model.system.po.Account;

public final class AccountEventArgs {

    private Account account;

    private String password;

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
