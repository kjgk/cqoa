package com.withub.model.std.po;

import com.withub.model.entity.AbstractBaseEntity;
import com.withub.model.system.po.Code;
import com.withub.model.system.po.User;

import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.util.Date;

@javax.persistence.Entity
@Table(name = "STD_AGENCY")
public class Agency extends AbstractBaseEntity {

    //================================ 属性声明 ==========================================================

    @OneToOne(targetEntity = User.class)
    @JoinColumn(name = "ownerId")
    private User owner;

    @OneToOne(targetEntity = User.class)
    @JoinColumn(name = "agentId")
    private User agent;

    private Date startTime;

    private Date endTime;

    @OneToOne(targetEntity = Code.class)
    @JoinColumn(name = "status")
    private Code status;

    private String reason;

    //================================ 属性方法 ==========================================================

    public User getOwner() {

        return owner;
    }

    public void setOwner(User owner) {

        this.owner = owner;
    }

    public User getAgent() {

        return agent;
    }

    public void setAgent(User agent) {

        this.agent = agent;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public Date getStartTime() {

        return startTime;
    }

    public void setStartTime(Date startTime) {

        this.startTime = startTime;
    }

    public Date getEndTime() {

        return endTime;
    }

    public void setEndTime(Date endTime) {

        this.endTime = endTime;
    }

    public Code getStatus() {

        return status;
    }

    public void setStatus(Code status) {

        this.status = status;
    }
}