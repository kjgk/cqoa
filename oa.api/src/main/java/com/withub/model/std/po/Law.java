package com.withub.model.std.po;


import com.withub.model.entity.AbstractBaseEntity;
import com.withub.model.std.FileUploadInfo;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@javax.persistence.Entity
@Table(name = "STD_LAW")
public class Law extends AbstractBaseEntity {

    //================================ 属性声明 ===========================================================

    private String name;

    @ManyToOne(targetEntity = LawCategory.class)
    @JoinColumn(name = "LawCategoryId")
    private LawCategory lawCategory;

    @OneToOne(targetEntity = LawIssueOrganization.class)
    @JoinColumn(name = "issueOrganizationID")
    private LawIssueOrganization issueOrganization;

    private Date issueDate;

    @OneToMany(targetEntity = FileInfo.class, mappedBy = "relatedObjectId", fetch = FetchType.LAZY)
    @OrderBy(value = "createTime asc")
    @Where(clause = "objectStatus = 1")
    private List<FileInfo> attachmentList;

    @Transient
    private List<FileUploadInfo> attachments;

    @Transient
    private String content;

    //================================ 属性方法 ===========================================================

    public String getName() {

        return name;
    }

    public void setName(String name) {

        this.name = name;
    }

    public LawCategory getLawCategory() {

        return lawCategory;
    }

    public void setLawCategory(LawCategory lawCategory) {

        this.lawCategory = lawCategory;
    }

    public LawIssueOrganization getIssueOrganization() {
        return issueOrganization;
    }

    public void setIssueOrganization(LawIssueOrganization issueOrganization) {
        this.issueOrganization = issueOrganization;
    }

    public Date getIssueDate() {

        return issueDate;
    }

    public void setIssueDate(Date issueDate) {

        this.issueDate = issueDate;
    }

    public List<FileInfo> getAttachmentList() {

        return attachmentList;
    }

    public void setAttachmentList(List<FileInfo> attachmentList) {

        this.attachmentList = attachmentList;
    }

    public List<FileUploadInfo> getAttachments() {

        return attachments;
    }

    public void setAttachments(List<FileUploadInfo> attachments) {

        this.attachments = attachments;
    }

    public String getContent() {

        return content;
    }

    public void setContent(String content) {

        this.content = content;
    }
}