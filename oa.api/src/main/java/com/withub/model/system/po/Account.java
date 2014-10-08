package com.withub.model.system.po;


import com.withub.model.entity.AbstractBaseEntity;
import com.withub.model.system.enumeration.AccountStatus;
import com.withub.model.system.enumeration.AccountType;

import javax.persistence.*;
import java.util.Date;

@javax.persistence.Entity
@Table(name = "SYS_ACCOUNT")
public class Account extends AbstractBaseEntity {

    //================================ 属性声明 ==========================================================

    private String name;

    @OneToOne(targetEntity = User.class)
    @JoinColumn(name = "UserId")
    private User user;

    private String salt;

    private String password;

    private Date passwordTime;

    @Enumerated(EnumType.STRING)
    private AccountType accountType;

    @Enumerated(EnumType.STRING)
    private AccountStatus accountStatus;

    //================================ 属性方法 ==========================================================

    public String getName() {

        return name;
    }

    public void setName(String name) {

        this.name = name;
    }

    public User getUser() {

        return user;
    }

    public void setUser(User user) {

        this.user = user;
    }

    public String getSalt() {

        return salt;
    }

    public void setSalt(String salt) {

        this.salt = salt;
    }

    public String getPassword() {

        return password;
    }

    public Date getPasswordTime() {

        return passwordTime;
    }

    public void setPasswordTime(Date passwordTime) {

        this.passwordTime = passwordTime;
    }

    public void setPassword(String password) {

        this.password = password;
    }

    public AccountType getAccountType() {

        return accountType;
    }

    public void setAccountType(AccountType accountType) {

        this.accountType = accountType;
    }

    public AccountStatus getAccountStatus() {

        return accountStatus;
    }

    public void setAccountStatus(AccountStatus accountStatus) {

        this.accountStatus = accountStatus;
    }

}