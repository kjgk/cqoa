package com.withub.model.std.po;

import com.withub.model.entity.AbstractBaseEntity;

import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@javax.persistence.Entity
@Table(name = "STD_DOCUMENTTYPE")
public class DocumentType extends AbstractBaseEntity {

    //=================== 属性声明 ============================================

    @ManyToOne(targetEntity = DocumentTypeCategory.class)
    @JoinColumn(name = "DocumentTypeCategoryId")
    private DocumentTypeCategory documentTypeCategory;

    private String code;

    private String name;

    private String documentTypeTag;

    private String description;

    private Integer orderNo;

    //=================== 属性方法 ============================================

    public DocumentTypeCategory getDocumentTypeCategory() {

        return documentTypeCategory;
    }

    public void setDocumentTypeCategory(DocumentTypeCategory documentTypeCategory) {

        this.documentTypeCategory = documentTypeCategory;
    }

    public String getCode() {

        return code;
    }

    public void setCode(String code) {

        this.code = code;
    }

    public String getName() {

        return name;
    }

    public void setName(String name) {

        this.name = name;
    }

    public String getDocumentTypeTag() {

        return documentTypeTag;
    }

    public void setDocumentTypeTag(String documentTypeTag) {

        this.documentTypeTag = documentTypeTag;
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