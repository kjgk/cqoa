package com.withub.model.std.po;


import com.withub.model.entity.AbstractBaseEntity;
import com.withub.model.std.FileUploadInfo;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.List;

@javax.persistence.Entity
@Table(name = "STD_FILETEMPLATE")
public class FileTemplate extends AbstractBaseEntity {

    //================================== 属性声明 =========================================================

    private String name;

    private String fileTemplateTag;

    private String configuration;

    @OneToMany(targetEntity = FileInfo.class, mappedBy = "relatedObjectId", fetch = FetchType.LAZY)
    @Where(clause = "objectStatus = 1")
    @OrderBy(value = "createTime desc")
    private List<FileInfo> attachmentList;

    @Transient
    private FileUploadInfo attachmentInfo;

    //================================ 属性方法 ===========================================================

    public String getName() {

        return name;
    }

    public void setName(String name) {

        this.name = name;
    }

    public String getFileTemplateTag() {

        return fileTemplateTag;
    }

    public void setFileTemplateTag(String fileTemplateTag) {

        this.fileTemplateTag = fileTemplateTag;
    }

    public String getConfiguration() {

        return configuration;
    }

    public void setConfiguration(String configuration) {

        this.configuration = configuration;
    }

    public List<FileInfo> getAttachmentList() {

        return attachmentList;
    }

    public void setAttachmentList(List<FileInfo> attachmentList) {

        this.attachmentList = attachmentList;
    }

    public FileUploadInfo getAttachmentInfo() {

        return attachmentInfo;
    }

    public void setAttachmentInfo(FileUploadInfo attachmentInfo) {

        this.attachmentInfo = attachmentInfo;
    }
}