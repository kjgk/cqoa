package com.withub.model.std.po;

import com.withub.model.entity.AbstractBaseEntity;

import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@javax.persistence.Entity
@Table(name = "STD_FILEINFO")
public class FileInfo extends AbstractBaseEntity {

    //============================== 属性声明 =============================================================

    @OneToOne(targetEntity = com.withub.model.system.po.Entity.class)
    @JoinColumn(name = "EntityId")
    private com.withub.model.system.po.Entity entity;

    private String relatedObjectId;

    private String name;

    private String path;

    private String fileName;

    private String fileExtension;

    private Long fileSize;

    private String fileMd5;

    private String description;

    private Integer orderNo;

    //=============================== 属性方法 ============================================================

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

    public String getName() {

        return name;
    }

    public void setName(String name) {

        this.name = name;
    }

    public String getPath() {

        return path;
    }

    public void setPath(String path) {

        this.path = path;
    }

    public String getFileName() {

        return fileName;
    }

    public void setFileName(String fileName) {

        this.fileName = fileName;
    }

    public String getFileExtension() {

        return fileExtension;
    }

    public void setFileExtension(String fileExtension) {

        this.fileExtension = fileExtension;
    }

    public Long getFileSize() {

        return fileSize;
    }

    public void setFileSize(Long fileSize) {

        this.fileSize = fileSize;
    }

    public String getFileMd5() {

        return fileMd5;
    }

    public void setFileMd5(String fileMd5) {

        this.fileMd5 = fileMd5;
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
