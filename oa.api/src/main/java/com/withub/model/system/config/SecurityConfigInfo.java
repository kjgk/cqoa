package com.withub.model.system.config;

/**
 * Security 配置信息
 */
public final class SecurityConfigInfo extends AbstractBaseConfigInfo {

    private static final long serialVersionUID = 1L;

    //====================== 属性声明 ==============================================

    private boolean requiredLoginValidateCode;

    private boolean allowChangeAccount;

    private int passwordLength;

    private String passwordStrength;

    private int passwordIntervalDays;

    private int passwordRepeatHistory;

    private boolean forceChangePassword;

    //====================== 属性方法 ===========================================

    public boolean isRequiredLoginValidateCode() {

        return requiredLoginValidateCode;
    }

    public void setRequiredLoginValidateCode(boolean requiredLoginValidateCode) {

        this.requiredLoginValidateCode = requiredLoginValidateCode;
    }

    public boolean isAllowChangeAccount() {

        return allowChangeAccount;
    }

    public void setAllowChangeAccount(boolean allowChangeAccount) {

        this.allowChangeAccount = allowChangeAccount;
    }

    public int getPasswordLength() {

        return passwordLength;
    }

    public void setPasswordLength(int passwordLength) {

        this.passwordLength = passwordLength;
    }

    public String getPasswordStrength() {

        return passwordStrength;
    }

    public void setPasswordStrength(String passwordStrength) {

        this.passwordStrength = passwordStrength;
    }

    public int getPasswordIntervalDays() {

        return passwordIntervalDays;
    }

    public void setPasswordIntervalDays(int passwordIntervalDays) {

        this.passwordIntervalDays = passwordIntervalDays;
    }

    public int getPasswordRepeatHistory() {

        return passwordRepeatHistory;
    }

    public void setPasswordRepeatHistory(int passwordRepeatHistory) {

        this.passwordRepeatHistory = passwordRepeatHistory;
    }

    public boolean isForceChangePassword() {

        return forceChangePassword;
    }

    public void setForceChangePassword(boolean forceChangePassword) {

        this.forceChangePassword = forceChangePassword;
    }
}
