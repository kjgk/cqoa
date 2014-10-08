package com.withub.model.workflow.po;

import com.withub.model.entity.AbstractEntity;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "WF_REGULATION")
public class WFRegulation extends AbstractEntity {

    private String objectType;

    private String relatedObjectId;

    private String descriptor;

    //=================== 属性方法 ============================================


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

    public String getDescriptor() {

        return descriptor;
    }

    public void setDescriptor(String descriptor) {

        this.descriptor = descriptor;
    }
}