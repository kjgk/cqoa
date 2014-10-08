package com.withub.model.std.po;

import com.withub.model.entity.AbstractBaseEntity;
import com.withub.model.system.po.Entity;

import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@javax.persistence.Entity
@Table(name = "STD_ENTITYCACHECONFIG")
public class EntityCacheConfig extends AbstractBaseEntity {

    //======================== 属性声明 ==============================

    private String name;

    @OneToOne(targetEntity = Entity.class)
    @JoinColumn(name = "EntityId")
    private Entity entity;

    private String cacheKey;

    private Integer cacheCount;

    private String timestampProperty;

    @OneToOne(targetEntity = DocumentType.class)
    @JoinColumn(name = "DocumentTypeId")
    private DocumentType documentType;

    private String addition;

    private Integer enable;

    //======================== 属性方法 ================================

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

    public String getCacheKey() {

        return cacheKey;
    }

    public void setCacheKey(String cacheKey) {

        this.cacheKey = cacheKey;
    }

    public Integer getCacheCount() {

        return cacheCount;
    }

    public void setCacheCount(Integer cacheCount) {

        this.cacheCount = cacheCount;
    }

    public String getTimestampProperty() {

        return timestampProperty;
    }

    public void setTimestampProperty(String timestampProperty) {

        this.timestampProperty = timestampProperty;
    }

    public DocumentType getDocumentType() {

        return documentType;
    }

    public void setDocumentType(DocumentType documentType) {

        this.documentType = documentType;
    }

    public String getAddition() {

        return addition;
    }

    public void setAddition(String addition) {

        this.addition = addition;
    }

    public Integer getEnable() {

        return enable;
    }

    public void setEnable(Integer enable) {

        this.enable = enable;
    }
}
