package com.withub.model.system.po;

import com.withub.model.entity.AbstractBaseEntity;

import javax.persistence.Table;

@javax.persistence.Entity
@Table(name = "SYS_USERSIGNATURE")
public class UserSignature extends AbstractBaseEntity {

    //=============================== 属性声明 ============================================================

    private String userId;

    private byte[] signature;

    private Long fileSize;

    //=============================== 属性方法 ============================================================

    public String getUserId() {

        return userId;
    }

    public void setUserId(String userId) {

        this.userId = userId;
    }

    public byte[] getSignature() {

        return signature;
    }

    public void setSignature(byte[] signature) {

        this.signature = signature;
    }

    public Long getFileSize() {

        return fileSize;
    }

    public void setFileSize(Long fileSize) {

        this.fileSize = fileSize;
    }
}
