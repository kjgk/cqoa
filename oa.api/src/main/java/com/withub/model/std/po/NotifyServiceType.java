package com.withub.model.std.po;


import com.withub.model.entity.AbstractBaseEntity;

import javax.persistence.Table;

@javax.persistence.Entity
@Table(name = "std_notifyservicetype")
public class NotifyServiceType extends AbstractBaseEntity {

    //======================= 属性声明 ============================================

    private String name;

    private String notifyServiceTypeTag;

    private String userPropertyAddress;

    private Integer customiseNotifyTime;

    private String description;

    private Integer enable;

    private Integer orderNo;

    //======================= 属性方法 ============================================

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNotifyServiceTypeTag() {
        return notifyServiceTypeTag;
    }

    public void setNotifyServiceTypeTag(String notifyServiceTypeTag) {
        this.notifyServiceTypeTag = notifyServiceTypeTag;
    }

    public String getUserPropertyAddress() {
        return userPropertyAddress;
    }

    public void setUserPropertyAddress(String userPropertyAddress) {
        this.userPropertyAddress = userPropertyAddress;
    }

    public Integer getCustomiseNotifyTime() {
        return customiseNotifyTime;
    }

    public void setCustomiseNotifyTime(Integer customiseNotifyTime) {
        this.customiseNotifyTime = customiseNotifyTime;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getEnable() {
        return enable;
    }

    public void setEnable(Integer enable) {
        this.enable = enable;
    }

    public Integer getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(Integer orderNo) {
        this.orderNo = orderNo;
    }
}
