package com.withub.model.std.po;


import com.withub.model.entity.AbstractBaseEntity;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "STD_FAVORITE")
public class Favorite extends AbstractBaseEntity {

    //=================== 属性声明 ===========================================

    private String name;

    private String url;

    @OneToOne(targetEntity = com.withub.model.system.po.Entity.class)
    @JoinColumn(name = "EntityId")
    private com.withub.model.system.po.Entity entity;

    private String entityInstanceId;

    private String description;

    private Integer orderNo;

    //=================== 属性方法 ===========================================

    public String getName() {

        return name;
    }

    public void setName(String name) {

        this.name = name;
    }

    public String getUrl() {

        return url;
    }

    public void setUrl(String url) {

        this.url = url;
    }

    public com.withub.model.system.po.Entity getEntity() {

        return entity;
    }

    public void setEntity(com.withub.model.system.po.Entity entity) {

        this.entity = entity;
    }

    public String getEntityInstanceId() {

        return entityInstanceId;
    }

    public void setEntityInstanceId(String entityInstanceId) {

        this.entityInstanceId = entityInstanceId;
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