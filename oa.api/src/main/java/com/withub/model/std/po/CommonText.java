package com.withub.model.std.po;


import com.withub.model.entity.AbstractBaseEntity;

import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@javax.persistence.Entity
@Table(name = "STD_COMMONTEXT")
public class CommonText extends AbstractBaseEntity {

    //================================ 属性声明 ==========================================================

    @OneToOne(targetEntity = com.withub.model.system.po.Entity.class)
    @JoinColumn(name = "EntityId")
    private com.withub.model.system.po.Entity entity;

    private String relatedObjectId;

    private String property;

    private String content;

    //================================ 属性方法 ==========================================================

    public com.withub.model.system.po.Entity getEntity() {

        return entity;
    }

    public void setEntity(com.withub.model.system.po.Entity entity) {

        this.entity = entity;
    }

    public String getRelatedObjectId() {

        return relatedObjectId;
    }

    public void setRelatedObjectId(String relatedObjectId) {

        this.relatedObjectId = relatedObjectId;
    }

    public String getProperty() {

        return property;
    }

    public void setProperty(String property) {

        this.property = property;
    }

    public String getContent() {

        return content;
    }

    public void setContent(String content) {

        this.content = content;
    }
}