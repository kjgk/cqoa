package com.withub.model.system.po;


import com.withub.model.entity.AbstractBaseEntity;

import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@javax.persistence.Entity
@Table(name = "SYS_ENTITYADMINISTRATOR")
public class EntityAdministrator extends AbstractBaseEntity {

    //=========================== 属性声明 =======================================

    @OneToOne(targetEntity = Entity.class)
    @JoinColumn(name = "EntityId")
    private Entity entity;

    private String entityInstanceId;

    private String objectType;

    private String relatedObjectId;

    private Integer orderNo;

    //=========================== 属性方法 =======================================

    public Entity getEntity() {

        return entity;
    }

    public void setEntity(Entity entity) {

        this.entity = entity;
    }

    public String getEntityInstanceId() {

        return entityInstanceId;
    }

    public void setEntityInstanceId(String entityInstanceId) {

        this.entityInstanceId = entityInstanceId;
    }

    public String getObjectType() {

        return objectType;
    }

    public void setObjectType(String objectType) {

        this.objectType = objectType;
    }

    public String getRelatedObjectId() {

        return relatedObjectId;
    }

    public void setRelatedObjectId(String relatedObjectId) {

        this.relatedObjectId = relatedObjectId;
    }

    public Integer getOrderNo() {

        return orderNo;
    }

    public void setOrderNo(Integer orderNo) {

        this.orderNo = orderNo;
    }
}