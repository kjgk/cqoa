package com.withub.model.std.po;


import com.withub.model.entity.AbstractBaseEntity;
import com.withub.model.system.po.Code;
import com.withub.model.system.po.Entity;

import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;


@javax.persistence.Entity
@Table(name = "STD_ENTITYTIMEEVENTMONITOR")
public class EntityTimeEventMonitor extends AbstractBaseEntity {

    //=====================================属性声明=========================================

    private String name;

    @OneToOne(targetEntity = Entity.class)
    @JoinColumn(name = "entityId")
    private Entity entity;

    /**
     * 是否过期事件
     */
    private Integer expiredEvent;

    /**
     * 实体对应的时间属性
     */
    private String entityTimeProperty;

    /**
     * 实体对应的时间属性是否是过期型的
     */
    private Integer entityTimePropertyIsExpired;

    /**
     * 过期时间值,如密码过期为30天,只对非过期事件有效
     */
    private Integer expiredTimeValue;

    @OneToOne(targetEntity = Code.class)
    @JoinColumn(name = "expiredTimeValueTimeUnitId")
    private Code expiredTimeValueTimeUnit;

    private String eventClassName;

    private Integer useWorkCalendar;

    private String startTime;

    private String endTime;

    private Integer intervalValue;

    @OneToOne(targetEntity = Code.class)
    @JoinColumn(name = "IntervalValueTimeUnitId")
    private Code intervalValueTimeUnit;

    private Integer priority;

    private Integer recordSetSize;

    private String additionalCondition;

    private Integer enable;

    private Integer orderNo;

    //=====================================属性方法=========================================

    public String getName() {

        return name;
    }

    public void setName(String name) {

        this.name = name;
    }

    public Entity getEntity() {

        return entity;
    }

    public void setEntity(Entity entity) {

        this.entity = entity;
    }

    public Integer getExpiredEvent() {

        return expiredEvent;
    }

    public void setExpiredEvent(Integer expiredEvent) {

        this.expiredEvent = expiredEvent;
    }

    public String getEntityTimeProperty() {

        return entityTimeProperty;
    }

    public void setEntityTimeProperty(String entityTimeProperty) {

        this.entityTimeProperty = entityTimeProperty;
    }

    public Integer getEntityTimePropertyIsExpired() {

        return entityTimePropertyIsExpired;
    }

    public void setEntityTimePropertyIsExpired(Integer entityTimePropertyIsExpired) {

        this.entityTimePropertyIsExpired = entityTimePropertyIsExpired;
    }

    public Integer getExpiredTimeValue() {

        return expiredTimeValue;
    }

    public void setExpiredTimeValue(Integer expiredTimeValue) {

        this.expiredTimeValue = expiredTimeValue;
    }

    public Code getExpiredTimeValueTimeUnit() {

        return expiredTimeValueTimeUnit;
    }

    public void setExpiredTimeValueTimeUnit(Code expiredTimeValueTimeUnit) {

        this.expiredTimeValueTimeUnit = expiredTimeValueTimeUnit;
    }

    public String getEventClassName() {

        return eventClassName;
    }

    public void setEventClassName(String eventClassName) {

        this.eventClassName = eventClassName;
    }

    public Integer getUseWorkCalendar() {

        return useWorkCalendar;
    }

    public void setUseWorkCalendar(Integer useWorkCalendar) {

        this.useWorkCalendar = useWorkCalendar;
    }

    public String getStartTime() {

        return startTime;
    }

    public void setStartTime(String startTime) {

        this.startTime = startTime;
    }

    public String getEndTime() {

        return endTime;
    }

    public void setEndTime(String endTime) {

        this.endTime = endTime;
    }

    public Integer getIntervalValue() {

        return intervalValue;
    }

    public void setIntervalValue(Integer intervalValue) {

        this.intervalValue = intervalValue;
    }

    public Code getIntervalValueTimeUnit() {

        return intervalValueTimeUnit;
    }

    public void setIntervalValueTimeUnit(Code intervalValueTimeUnit) {

        this.intervalValueTimeUnit = intervalValueTimeUnit;
    }

    public Integer getPriority() {

        return priority;
    }

    public void setPriority(Integer priority) {

        this.priority = priority;
    }

    public Integer getRecordSetSize() {

        return recordSetSize;
    }

    public void setRecordSetSize(Integer recordSetSize) {

        this.recordSetSize = recordSetSize;
    }

    public String getAdditionalCondition() {

        return additionalCondition;
    }

    public void setAdditionalCondition(String additionalCondition) {

        this.additionalCondition = additionalCondition;
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
