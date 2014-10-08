package com.withub.model.std.po;

import com.withub.model.entity.AbstractBaseEntity;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;


@Entity
@Table(name = "STD_EVENTNOTIFYSERVICETYPE")
public class EventNotifyServiceType extends AbstractBaseEntity {

    //=================== 属性声明 ============================================

    @OneToOne(targetEntity = SystemEvent.class)
    @JoinColumn(name = "SystemEventId")
    private SystemEvent systemEvent;

    @OneToOne(targetEntity = NotifyServiceType.class)
    @JoinColumn(name = "NotifyServiceTypeId")
    private NotifyServiceType notifyServiceType;

    private String titleTemplate;

    private String contentTemplate;

    private Integer defaultTemplate;

    private Integer orderNo;

    private Integer enable;

    //=================== 属性方法 ============================================

    public SystemEvent getSystemEvent() {
        return systemEvent;
    }

    public void setSystemEvent(SystemEvent systemEvent) {
        this.systemEvent = systemEvent;
    }

    public NotifyServiceType getNotifyServiceType() {
        return notifyServiceType;
    }

    public void setNotifyServiceType(NotifyServiceType notifyServiceType) {
        this.notifyServiceType = notifyServiceType;
    }

    public String getTitleTemplate() {
        return titleTemplate;
    }

    public void setTitleTemplate(String titleTemplate) {
        this.titleTemplate = titleTemplate;
    }

    public String getContentTemplate() {
        return contentTemplate;
    }

    public void setContentTemplate(String contentTemplate) {
        this.contentTemplate = contentTemplate;
    }

    public Integer getDefaultTemplate() {
        return defaultTemplate;
    }

    public void setDefaultTemplate(Integer defaultTemplate) {
        this.defaultTemplate = defaultTemplate;
    }

    public Integer getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(Integer orderNo) {
        this.orderNo = orderNo;
    }

    public Integer getEnable() {

        return enable;
    }

    public void setEnable(Integer enable) {

        this.enable = enable;
    }
}
