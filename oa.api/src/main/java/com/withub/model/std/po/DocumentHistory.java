package com.withub.model.std.po;


import com.withub.model.entity.AbstractBaseEntity;
import com.withub.model.std.FileUploadInfo;
import com.withub.model.system.po.Code;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "STD_DOCUMENTHISTORY")
public class DocumentHistory extends AbstractBaseEntity {

    //================= 属性声明 ========================================

    private String name;

    private String pinYin;

    private String keyword;

    @ManyToOne(targetEntity = Document.class)
    @JoinColumn(name = "DocumentId")
    private Document document;

    private String version;

    private String description;

    @OneToOne(targetEntity = Code.class)
    @JoinColumn(name = "SecrecyLevelId")
    private Code secrecyLevel;

    private Date writeDate;

    @OneToOne(targetEntity = Code.class)
    @JoinColumn(name = "Status")
    private Code status;

    @OneToMany(targetEntity = DocumentHistoryAuthor.class, mappedBy = "documentHistory", fetch = FetchType.LAZY)
    @OrderBy(value = "orderNo asc")
    private List<DocumentHistoryAuthor> authorList;

    @OneToMany(targetEntity = FileInfo.class, mappedBy = "relatedObjectId", fetch = FetchType.EAGER)
    @Where(clause = "objectStatus = 1")
    @OrderBy(value = "createTime desc")
    private List<FileInfo> attachmentList;

    @Transient
    private FileUploadInfo attachmentInfo;

    //================= 属性方法 ========================================

    public String getName() {

        return name;
    }

    public void setName(String name) {

        this.name = name;
    }

    public String getPinYin() {

        return pinYin;
    }

    public void setPinYin(String pinYin) {

        this.pinYin = pinYin;
    }

    public String getKeyword() {

        return keyword;
    }

    public void setKeyword(String keyword) {

        this.keyword = keyword;
    }

    public Document getDocument() {

        return document;
    }

    public void setDocument(Document document) {

        this.document = document;
    }

    public String getVersion() {

        return version;
    }

    public void setVersion(String version) {

        this.version = version;
    }

    public String getDescription() {

        return description;
    }

    public void setDescription(String description) {

        this.description = description;
    }

    public Code getSecrecyLevel() {

        return secrecyLevel;
    }

    public void setSecrecyLevel(Code secrecyLevel) {

        this.secrecyLevel = secrecyLevel;
    }

    public Date getWriteDate() {

        return writeDate;
    }

    public void setWriteDate(Date writeDate) {

        this.writeDate = writeDate;
    }

    public Code getStatus() {

        return status;
    }

    public void setStatus(Code status) {

        this.status = status;
    }

    public List<DocumentHistoryAuthor> getAuthorList() {

        return authorList;
    }

    public void setAuthorList(List<DocumentHistoryAuthor> authorList) {

        this.authorList = authorList;
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