package com.withub.model.system.po;

import com.withub.model.entity.AbstractEntity;

import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.util.Date;

@javax.persistence.Entity
@Table(name = "SYS_SECURITYLOG")
public class SecurityLog extends AbstractEntity {

    //================================ 属性声明 ===========================================================

    @OneToOne(targetEntity = User.class)
    @JoinColumn(name = "UserId")
    private User user;

    @OneToOne(targetEntity = Code.class)
    @JoinColumn(name = "LoginTypeId")
    private Code loginType;

    private String clientIdentityCode;

    private String onlineUserId;

    private Long ip256Value;

    private Date loginTime;

    @OneToOne(targetEntity = Code.class)
    @JoinColumn(name = "LogoutTypeId")
    private Code logoutType;

    private Date logoutTime;

    //================================ 属性方法 ===========================================================

    public User getUser() {

        return user;
    }

    public void setUser(User user) {

        this.user = user;
    }

    public Code getLoginType() {

        return loginType;
    }

    public void setLoginType(Code loginType) {

        this.loginType = loginType;
    }

    public String getClientIdentityCode() {

        return clientIdentityCode;
    }

    public void setClientIdentityCode(String clientIdentityCode) {

        this.clientIdentityCode = clientIdentityCode;
    }

    public Long getIp256Value() {

        return ip256Value;
    }

    public void setIp256Value(Long ip256Value) {

        this.ip256Value = ip256Value;
    }

    public Date getLoginTime() {

        return loginTime;
    }

    public void setLoginTime(Date loginTime) {

        this.loginTime = loginTime;
    }

    public Date getLogoutTime() {

        return logoutTime;
    }

    public void setLogoutTime(Date logoutTime) {

        this.logoutTime = logoutTime;
    }

    public String getOnlineUserId() {
        return onlineUserId;
    }

    public void setOnlineUserId(String onlineUserId) {
        this.onlineUserId = onlineUserId;
    }

    public Code getLogoutType() {
        return logoutType;
    }

    public void setLogoutType(Code logoutType) {
        this.logoutType = logoutType;
    }
}