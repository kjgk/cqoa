package com.withub.model.std.po;

import com.withub.model.entity.AbstractBaseEntity;
import com.withub.model.system.po.Organization;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@javax.persistence.Entity
@Table(name = "STD_DOCUMENT")
public class Document extends AbstractBaseEntity {

    //=================== 属性声明 ============================================

    @OneToOne(targetEntity = DocumentType.class)
    @JoinColumn(name = "DocumentTypeId")
    private DocumentType documentType;

    @OneToOne(targetEntity = Organization.class)
    @JoinColumn(name = "OrganizationId")
    private Organization organization;

    @OneToOne(targetEntity = DocumentHistory.class)
    @JoinColumn(name = "FirstHistoryId")
    private DocumentHistory firstHistory;

    @OneToOne(targetEntity = DocumentHistory.class)
    @JoinColumn(name = "LastHistoryId")
    private DocumentHistory lastHistory;

    private Integer historyCount = 0;

    @OneToMany(targetEntity = DocumentHistory.class, mappedBy = "document", fetch = FetchType.LAZY)
    @OrderBy(value = "createTime desc")
    @Where(clause = "objectStatus = 1")
    private List<DocumentHistory> historyList = new ArrayList<DocumentHistory>();

    //=================== 属性方法 ============================================


    public DocumentType getDocumentType() {
        return documentType;
    }

    public void setDocumentType(DocumentType documentType) {
        this.documentType = documentType;
    }

    public Organization getOrganization() {
        return organization;
    }

    public void setOrganization(Organization organization) {
        this.organization = organization;
    }

    public DocumentHistory getFirstHistory() {
        return firstHistory;
    }

    public void setFirstHistory(DocumentHistory firstHistory) {
        this.firstHistory = firstHistory;
    }

    public DocumentHistory getLastHistory() {
        return lastHistory;
    }

    public void setLastHistory(DocumentHistory lastHistory) {
        this.lastHistory = lastHistory;
    }

    public Integer getHistoryCount() {
        return historyCount;
    }

    public void setHistoryCount(Integer historyCount) {
        this.historyCount = historyCount;
    }

    public List<DocumentHistory> getHistoryList() {
        return historyList;
    }

    public void setHistoryList(List<DocumentHistory> historyList) {
        this.historyList = historyList;
    }
}