package com.withub.model.std.po;

import com.withub.model.entity.AbstractEntity;
import com.withub.model.system.po.User;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(name = "std_sendqueue")
public class SendQueue extends AbstractEntity {

    //===================================属性声明====================================

    @OneToOne(targetEntity = NotifyQueueHistory.class)
    @JoinColumn(name = "notifyQueueId")
    private NotifyQueueHistory notifyQueue;

    @OneToOne(targetEntity = NotifyServiceType.class)
    @JoinColumn(name = "notifyServiceTypeId")
    private NotifyServiceType notifyServiceType;

    @OneToOne(targetEntity = User.class)
    @JoinColumn(name = "userId")
    private User user;

    private String address;

    private String title;

    private String content;

    private Date enterTime;

    private Date lastSendTime;

    private Integer sendTimes;

    //==================================属性方法=====================================


    public NotifyQueueHistory getNotifyQueue() {

        return notifyQueue;
    }

    public void setNotifyQueue(NotifyQueueHistory notifyQueue) {

        this.notifyQueue = notifyQueue;
    }

    public NotifyServiceType getNotifyServiceType() {
        return notifyServiceType;
    }

    public void setNotifyServiceType(NotifyServiceType notifyServiceType) {
        this.notifyServiceType = notifyServiceType;
    }

    public User getUser() {

        return user;
    }

    public void setUser(User user) {

        this.user = user;
    }

    public String getAddress() {

        return address;
    }

    public void setAddress(String address) {

        this.address = address;
    }

    public String getTitle() {

        return title;
    }

    public void setTitle(String title) {

        this.title = title;
    }

    public String getContent() {

        return content;
    }

    public void setContent(String content) {

        this.content = content;
    }

    public Date getEnterTime() {

        return enterTime;
    }

    public void setEnterTime(Date enterTime) {

        this.enterTime = enterTime;
    }

    public Date getLastSendTime() {

        return lastSendTime;
    }

    public void setLastSendTime(Date lastSendTime) {

        this.lastSendTime = lastSendTime;
    }

    public Integer getSendTimes() {

        return sendTimes;
    }

    public void setSendTimes(Integer sendTimes) {

        this.sendTimes = sendTimes;
    }
}
