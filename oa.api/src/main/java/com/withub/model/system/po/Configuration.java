package com.withub.model.system.po;

import com.withub.model.entity.AbstractBaseEntity;

import javax.persistence.Table;

@javax.persistence.Entity
@Table(name = "SYS_CONFIGURATION")
public class Configuration extends AbstractBaseEntity {

    private static final long serialVersionUID = 1L;

    //=================== 属性声明 ============================================

    private String className;

    private String relatedObjectId;

    private String value;

    //=================== 属性方法 ============================================

    public String getClassName() {

        return className;
    }

    public void setClassName(String className) {

        this.className = className;
    }

    public String getRelatedObjectId() {

        return relatedObjectId;
    }

    public void setRelatedObjectId(String relatedObjectId) {

        this.relatedObjectId = relatedObjectId;
    }

    public String getValue() {

        return value;
    }

    public void setValue(String value) {

        this.value = value;
    }
}
