package com.withub.model.security;

import com.withub.model.security.enumeration.LoginValidateResult;
import com.withub.model.system.po.Account;

public final class LoginResult {

    private LoginValidateResult validateResult;

    private Account account;

    public LoginValidateResult getValidateResult() {

        return validateResult;
    }

    public void setValidateResult(LoginValidateResult validateResult) {

        this.validateResult = validateResult;
    }

    public Account getAccount() {

        return account;
    }

    public void setAccount(Account account) {

        this.account = account;
    }
}
