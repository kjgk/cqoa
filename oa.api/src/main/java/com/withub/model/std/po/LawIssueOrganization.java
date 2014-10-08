package com.withub.model.std.po;


import com.withub.model.entity.AbstractRecursionEntity;
import com.withub.model.std.FileUploadInfo;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "STD_LAWISSUEORGANIZATION")
public class LawIssueOrganization extends AbstractRecursionEntity {

    //================================ 属性声明 ===========================================================

    @ManyToOne(targetEntity = LawIssueOrganization.class)
    @JoinColumn(name = "ParentId")
    private LawIssueOrganization parent;

    @OneToMany(targetEntity = LawIssueOrganization.class, mappedBy = "parent", fetch = FetchType.LAZY)
    @OrderBy(value = "orderNo asc")
    @Where(clause = "objectStatus = 1")
    private List<LawIssueOrganization> childList = new ArrayList<LawIssueOrganization>();

    @OneToMany(targetEntity = FileInfo.class, mappedBy = "relatedObjectId", fetch = FetchType.LAZY)
    @OrderBy(value = "createTime asc")
    @Where(clause = "objectStatus = 1")
    private List<FileInfo> attachmentList;

    @Transient
    private List<FileUploadInfo> attachments;

    @Transient
    private String content;

    //================================ 属性方法 ===========================================================


    public LawIssueOrganization getParent() {

        return parent;
    }

    public void setParent(LawIssueOrganization parent) {

        this.parent = parent;
    }

    public List<LawIssueOrganization> getChildList() {

        return childList;
    }

    public void setChildList(List<LawIssueOrganization> childList) {

        this.childList = childList;
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