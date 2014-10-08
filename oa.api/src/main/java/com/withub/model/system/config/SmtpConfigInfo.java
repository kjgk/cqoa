package com.withub.model.system.config;

/**
 * SMTP 配置信息
 */
public final class SmtpConfigInfo extends AbstractBaseConfigInfo {

    private static final long serialVersionUID = 1L;

    //================= 属性声明 ==============================================

    /**
     * 服务器IP
     */
    private String host;

    /**
     * 端口, 默认为 25
     */
    private int port = 25;

    /**
     * 是否要验证
     */
    private boolean certificateRequired = true;

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    /**
     * 邮箱地址
     */
    private String mailAddress;

    /**
     * 显示名称
     */
    private String displayName;

    /**
     * 返回地址
     */
    private String returnAddress;

    /**
     * 接受邮件的协议类型
     */
    private String protocolType;

    /**
     * 只接受指定发送邮件的用户名，多个以分号隔开
     */
    private String sendEmailUserName;


    //================== 属性 方法 ===========================================

    public String getHost() {

        return host;
    }

    public void setHost(String host) {

        this.host = host;
    }

    public int getPort() {

        return port;
    }

    public void setPort(int port) {

        this.port = port;
    }

    public boolean getCertificateRequired() {

        return certificateRequired;
    }

    public void setCertificateRequired(boolean certificateRequired) {

        this.certificateRequired = certificateRequired;
    }

    public String getMailAddress() {

        return mailAddress;
    }

    public void setMailAddress(String mailAddress) {

        this.mailAddress = mailAddress;
    }

    public String getPassword() {

        return password;
    }

    public void setPassword(String password) {

        this.password = password;
    }

    public String getUsername() {

        return username;
    }

    public void setUsername(String username) {

        this.username = username;
    }

    public String getDisplayName() {

        return displayName;
    }

    public void setDisplayName(String displayName) {

        this.displayName = displayName;
    }

    public String getReturnAddress() {

        return returnAddress;
    }

    public void setReturnAddress(String returnAddress) {

        this.returnAddress = returnAddress;
    }

    public String getProtocolType() {

        return protocolType;
    }

    public void setProtocolType(String protocolType) {

        this.protocolType = protocolType;
    }

    public String getSendEmailUserName() {

        return sendEmailUserName;
    }

    public void setSendEmailUserName(String sendEmailUserName) {

        this.sendEmailUserName = sendEmailUserName;
    }
}
