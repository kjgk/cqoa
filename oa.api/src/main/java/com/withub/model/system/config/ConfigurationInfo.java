package com.withub.model.system.config;

public class ConfigurationInfo {

    private SmtpConfigInfo smtpConfigInfo;

    private SystemConfigInfo systemConfigInfo;

    private SecurityConfigInfo securityConfigInfo;

    private SmsConfigInfo smsConfigInfo;

    private String currentUserId;

    public SmtpConfigInfo getSmtpConfigInfo() {

        return smtpConfigInfo;
    }

    public void setSmtpConfigInfo(SmtpConfigInfo smtpConfigInfo) {

        this.smtpConfigInfo = smtpConfigInfo;
    }

    public SystemConfigInfo getSystemConfigInfo() {

        return systemConfigInfo;
    }

    public void setSystemConfigInfo(SystemConfigInfo systemConfigInfo) {

        this.systemConfigInfo = systemConfigInfo;
    }

    public SecurityConfigInfo getSecurityConfigInfo() {

        return securityConfigInfo;
    }

    public void setSecurityConfigInfo(SecurityConfigInfo securityConfigInfo) {

        this.securityConfigInfo = securityConfigInfo;
    }

    public SmsConfigInfo getSmsConfigInfo() {

        return smsConfigInfo;
    }

    public void setSmsConfigInfo(SmsConfigInfo smsConfigInfo) {

        this.smsConfigInfo = smsConfigInfo;
    }

    public String getCurrentUserId() {

        return currentUserId;
    }

    public void setCurrentUserId(String currentUserId) {

        this.currentUserId = currentUserId;
    }
}
