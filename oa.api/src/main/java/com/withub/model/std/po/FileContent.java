package com.withub.model.std.po;

import com.withub.model.entity.AbstractBaseEntity;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "STD_FILECONTENT")
public class FileContent extends AbstractBaseEntity {

    //======================== 属性声明 ==============================================

    private String fileInfoId;

    private byte[] content;

    //======================== 属性方法 ==============================================

    public String getFileInfoId() {

        return fileInfoId;
    }

    public void setFileInfoId(String fileInfoId) {

        this.fileInfoId = fileInfoId;
    }

    public byte[] getContent() {

        return content;
    }

    public void setContent(byte[] content) {

        this.content = content;
    }
}
