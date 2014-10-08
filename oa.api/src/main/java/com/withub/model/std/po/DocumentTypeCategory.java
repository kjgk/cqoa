package com.withub.model.std.po;

import com.withub.model.entity.AbstractRecursionEntity;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "STD_DOCUMENTTYPECATEGORY")
public class DocumentTypeCategory extends AbstractRecursionEntity {

    //=================== 属性声明 ============================================

    private String code;

    @ManyToOne(targetEntity = DocumentTypeCategory.class)
    @JoinColumn(name = "ParentId")
    private DocumentTypeCategory parent;

    @OneToMany(targetEntity = DocumentTypeCategory.class, mappedBy = "parent", fetch = FetchType.LAZY)
    @OrderBy(value = "orderNo asc")
    @Where(clause = "objectStatus = 1")
    private List<DocumentTypeCategory> childList = new ArrayList<DocumentTypeCategory>();

    @OneToMany(targetEntity = DocumentType.class, mappedBy = "documentTypeCategory", fetch = FetchType.LAZY)
    @OrderBy(value = "orderNo asc")
    @Where(clause = "objectStatus = 1")
    private List<DocumentType> documentTypeList;

    //=================== 属性方法 ============================================

    public String getCode() {

        return code;
    }

    public void setCode(String code) {

        this.code = code;
    }

    public DocumentTypeCategory getParent() {

        return parent;
    }

    public void setParent(DocumentTypeCategory parent) {

        this.parent = parent;
    }

    public List<DocumentTypeCategory> getChildList() {

        return childList;
    }

    public void setChildList(List<DocumentTypeCategory> childList) {

        this.childList = childList;
    }

    public List<DocumentType> getDocumentTypeList() {

        return documentTypeList;
    }

    public void setDocumentTypeList(List<DocumentType> documentTypeList) {

        this.documentTypeList = documentTypeList;
    }
}