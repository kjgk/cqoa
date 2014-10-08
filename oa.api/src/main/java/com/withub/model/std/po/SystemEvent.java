package com.withub.model.std.po;

import com.withub.model.entity.AbstractBaseEntity;
import com.withub.model.system.po.Code;

import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@javax.persistence.Entity
@Table(name = "STD_SYSTEMEVENT")
public class SystemEvent extends AbstractBaseEntity {

    //======================== 属性声明 ================================================

    private String name;

    private String className;

    private Integer shouldSerializable;

    private String entityProperty;

    private Integer enableNotify;

    private Integer priority;

    private Integer delay;

    private Integer intervalValue;

    @OneToOne(targetEntity = Code.class)
    @JoinColumn(name = "intervalTimeUnitId")
    private Code intervalTimeUnit;

    private Integer retrySendCount;

    private String accepterServiceMethod;

    private String userProperty;

    private String description;

    private Integer orderNo;

    //======================== 属性方法 =============================================================


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public Integer getShouldSerializable() {
        return shouldSerializable;
    }

    public void setShouldSerializable(Integer shouldSerializable) {
        this.shouldSerializable = shouldSerializable;
    }

    public String getEntityProperty() {
        return entityProperty;
    }

    public void setEntityProperty(String entityProperty) {
        this.entityProperty = entityProperty;
    }

    public Integer getEnableNotify() {
        return enableNotify;
    }

    public void setEnableNotify(Integer enableNotify) {
        this.enableNotify = enableNotify;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public Integer getDelay() {
        return delay;
    }

    public void setDelay(Integer delay) {
        this.delay = delay;
    }

    public Integer getIntervalValue() {
        return intervalValue;
    }

    public void setIntervalValue(Integer intervalValue) {
        this.intervalValue = intervalValue;
    }

    public Code getIntervalTimeUnit() {
        return intervalTimeUnit;
    }

    public void setIntervalTimeUnit(Code intervalTimeUnit) {
        this.intervalTimeUnit = intervalTimeUnit;
    }

    public Integer getRetrySendCount() {
        return retrySendCount;
    }

    public void setRetrySendCount(Integer retrySendCount) {
        this.retrySendCount = retrySendCount;
    }

    public String getAccepterServiceMethod() {
        return accepterServiceMethod;
    }

    public void setAccepterServiceMethod(String accepterServiceMethod) {
        this.accepterServiceMethod = accepterServiceMethod;
    }

    public String getUserProperty() {
        return userProperty;
    }

    public void setUserProperty(String userProperty) {
        this.userProperty = userProperty;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(Integer orderNo) {
        this.orderNo = orderNo;
    }
}
