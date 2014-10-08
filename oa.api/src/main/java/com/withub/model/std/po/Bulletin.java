package com.withub.model.std.po;

import com.withub.model.entity.AbstractBaseEntity;
import com.withub.model.std.FileUploadInfo;
import com.withub.model.system.po.Code;
import com.withub.model.system.po.Organization;
import com.withub.model.system.po.User;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@javax.persistence.Entity
@Table(name = "STD_Bulletin")
public class Bulletin extends AbstractBaseEntity {

    //================================= 属性声明 ==========================================================

    private String title;

    @OneToOne(targetEntity = Organization.class)
    @JoinColumn(name = "OrganizationId")
    private Organization organization;

    @OneToOne(targetEntity = Code.class)
    @JoinColumn(name = "BulletinTypeId")
    private Code bulletinType;

    @OneToOne(targetEntity = Code.class)
    @JoinColumn(name = "BulletinLevelId")
    private Code bulletinLevel;

    private Date effectiveTime;

    private Date issueTime;

    @OneToOne(targetEntity = Code.class)
    @JoinColumn(name = "status")
    private Code status;

    @OneToOne(targetEntity = Organization.class)
    @JoinColumn(name = "IssueOrganizationId")
    private Organization issueOrganization;

    @OneToOne(targetEntity = User.class)
    @JoinColumn(name = "issuer")
    private User issuer;

    @OneToMany(targetEntity = FileInfo.class, mappedBy = "relatedObjectId", fetch = FetchType.LAZY)
    @OrderBy(value = "createTime asc")
    @Where(clause = "objectStatus = 1")
    private List<FileInfo> attachmentList;

    @Transient
    private List<FileUploadInfo> attachments;

    @Transient
    private String content;

    //==================================== 属性方法 =======================================================

    public String getTitle() {

        return title;
    }

    public void setTitle(String title) {

        this.title = title;
    }

    public Organization getOrganization() {

        return organization;
    }

    public void setOrganization(Organization organization) {

        this.organization = organization;
    }

    public Code getBulletinType() {

        return bulletinType;
    }

    public void setBulletinType(Code bulletinType) {

        this.bulletinType = bulletinType;
    }

    public Code getBulletinLevel() {

        return bulletinLevel;
    }

    public void setBulletinLevel(Code bulletinLevel) {

        this.bulletinLevel = bulletinLevel;
    }

    public Date getEffectiveTime() {

        return effectiveTime;
    }

    public void setEffectiveTime(Date effectiveTime) {

        this.effectiveTime = effectiveTime;
    }

    public Date getIssueTime() {

        return issueTime;
    }

    public void setIssueTime(Date issueTime) {

        this.issueTime = issueTime;
    }

    public Code getStatus() {

        return status;
    }

    public void setStatus(Code status) {

        this.status = status;
    }

    public Organization getIssueOrganization() {

        return issueOrganization;
    }

    public void setIssueOrganization(Organization issueOrganization) {

        this.issueOrganization = issueOrganization;
    }

    public User getIssuer() {

        return issuer;
    }

    public void setIssuer(User issuer) {

        this.issuer = issuer;
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