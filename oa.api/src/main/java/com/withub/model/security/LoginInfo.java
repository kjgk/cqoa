package com.withub.model.security;

/**
 * 登录信息
 */
public final class LoginInfo {

    /**
     * 登录类型
     */
    private String loginType;

    /**
     * 客户端身份代码
     * 如 : IP, PIN
     */
    private String clientIdentityCode;

    /**
     * 帐号名称
     */
    private String accountName;

    /**
     * 密码
     */
    private String password;

    /**
     * 检验码
     */
    private String validateCode;

    //================= 属性方法 ===============================================

    public String getLoginType() {

        return loginType;
    }

    public void setLoginType(String loginType) {

        this.loginType = loginType;
    }

    public String getClientIdentityCode() {

        return clientIdentityCode;
    }

    public void setClientIdentityCode(String clientIdentityCode) {

        this.clientIdentityCode = clientIdentityCode;
    }

    public String getAccountName() {

        return accountName;
    }

    public void setAccountName(String accountName) {

        this.accountName = accountName;
    }

    public String getPassword() {

        return password;
    }

    public void setPassword(String password) {

        this.password = password;
    }

    public String getValidateCode() {

        return validateCode;
    }

    public void setValidateCode(String validateCode) {

        this.validateCode = validateCode;
    }
}
