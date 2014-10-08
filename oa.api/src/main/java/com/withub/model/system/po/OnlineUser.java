package com.withub.model.system.po;


import com.withub.model.entity.AbstractEntity;

import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.util.Date;

@javax.persistence.Entity
@Table(name = "SYS_ONLINEUSER")
public class OnlineUser extends AbstractEntity {
    //================================ 属性声明 ===========================================================

    @OneToOne(targetEntity = User.class)
    @JoinColumn(name = "UserId")
    private User user;

    @OneToOne(targetEntity = Account.class)
    @JoinColumn(name = "accountId")
    private Account account;

    @OneToOne(targetEntity = Code.class)
    @JoinColumn(name = "LoginTypeId")
    private Code loginType;

    private String clientIdentityCode;

    private Date loginTime;

    private Long ip256Value;

    //================================ 属性方法 ===========================================================

    public User getUser() {

        return user;
    }

    public void setUser(User user) {

        this.user = user;
    }

    public Account getAccount() {

        return account;
    }

    public void setAccount(Account account) {

        this.account = account;
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

    public Date getLoginTime() {

        return loginTime;
    }

    public void setLoginTime(Date loginTime) {

        this.loginTime = loginTime;
    }

    public Long getIp256Value() {

        return ip256Value;
    }

    public void setIp256Value(Long ip256Value) {

        this.ip256Value = ip256Value;
    }

}
