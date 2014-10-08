package com.withub.model.system.po;


import com.withub.model.entity.AbstractBaseEntity;

import javax.persistence.Table;
import java.util.Date;

@javax.persistence.Entity
@Table(name = "SYS_PASSWORDHISTORY")
public class PasswordHistory extends AbstractBaseEntity {

    //=================== 属性声明 ============================================

    private String accountId;

    private String password;

    private Date passwordTime;

    //=================== 属性方法 ============================================

    public String getAccountId() {

        return accountId;
    }

    public void setAccountId(String accountId) {

        this.accountId = accountId;
    }

    public String getPassword() {

        return password;
    }

    public void setPassword(String password) {

        this.password = password;
    }

    public Date getPasswordTime() {

        return passwordTime;
    }

    public void setPasswordTime(Date passwordTime) {

        this.passwordTime = passwordTime;
    }
}