package com.withub.model.std.po;


import com.withub.model.entity.AbstractBaseEntity;
import com.withub.model.system.po.User;

import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.util.Date;

@javax.persistence.Entity
@Table(name = "STD_BLACKLIST")
public class Blacklist extends AbstractBaseEntity {

    //================================ 属性声明 ==========================================================

    @OneToOne(targetEntity = User.class)
    @JoinColumn(name = "UserId")
    private User user;

    private Date enterTime;

    private Date removeTime;

    private String description;

    private Integer status;

    //================================ 属性方法 ==========================================================

    public User getUser() {

        return user;
    }

    public void setUser(User user) {

        this.user = user;
    }

    public Date getEnterTime() {

        return enterTime;
    }

    public void setEnterTime(Date enterTime) {

        this.enterTime = enterTime;
    }

    public Date getRemoveTime() {

        return removeTime;
    }

    public void setRemoveTime(Date removeTime) {

        this.removeTime = removeTime;
    }

    public String getDescription() {

        return description;
    }

    public void setDescription(String description) {

        this.description = description;
    }

    public Integer getStatus() {

        return status;
    }

    public void setStatus(Integer status) {

        this.status = status;
    }
}