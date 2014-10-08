package com.withub.model.system.po;

import com.withub.model.entity.AbstractBaseEntity;

import javax.persistence.Table;

@javax.persistence.Entity
@Table(name = "SYS_USERPHOTO")
public class UserPhoto extends AbstractBaseEntity {

    //=============================== 属性声明 ============================================================

    private String userId;

    private byte[] content;

    //=============================== 属性方法 ============================================================

    public String getUserId() {

        return userId;
    }

    public void setUserId(String userId) {

        this.userId = userId;
    }

    public byte[] getContent() {

        return content;
    }

    public void setContent(byte[] content) {

        this.content = content;
    }
}
