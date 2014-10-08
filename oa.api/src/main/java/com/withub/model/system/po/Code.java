package com.withub.model.system.po;


import com.withub.model.entity.AbstractBaseEntity;

import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@javax.persistence.Entity
@Table(name = "SYS_CODE")
public class Code extends AbstractBaseEntity {

    private String name;

    private String codeTag;

    private Integer defaultValue;

    private Integer statusValue;

    private Integer orderNo;

    private String description;

    @ManyToOne(targetEntity = CodeColumn.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "CodeColumnId")
    private CodeColumn codeColumn;

    //===================== 属性方法 ================================

    public String getName() {

        return name;
    }

    public void setName(String name) {

        this.name = name;
    }

    public String getCodeTag() {

        return codeTag;
    }

    public void setCodeTag(String codeTag) {

        this.codeTag = codeTag;
    }

    public Integer getDefaultValue() {

        return defaultValue;
    }

    public void setDefaultValue(Integer defaultValue) {

        this.defaultValue = defaultValue;
    }

    public Integer getStatusValue() {

        return statusValue;
    }

    public void setStatusValue(Integer statusValue) {

        this.statusValue = statusValue;
    }

    public Integer getOrderNo() {

        return orderNo;
    }

    public void setOrderNo(Integer orderNo) {

        this.orderNo = orderNo;
    }

    public String getDescription() {

        return description;
    }

    public void setDescription(String description) {

        this.description = description;
    }

    public CodeColumn getCodeColumn() {

        return codeColumn;
    }

    public void setCodeColumn(CodeColumn codeColumn) {

        this.codeColumn = codeColumn;
    }
}