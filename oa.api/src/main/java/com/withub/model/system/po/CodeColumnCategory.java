package com.withub.model.system.po;

import com.withub.model.entity.AbstractRecursionEntity;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@javax.persistence.Entity
@Table(name = "SYS_CODECOLUMNCATEGORY")
public class CodeColumnCategory extends AbstractRecursionEntity {

    //=================== 属性声明 ===========================================

    @ManyToOne(targetEntity = CodeColumnCategory.class)
    @JoinColumn(name = "parentId")
    private CodeColumnCategory parent;

    @OneToMany(targetEntity = CodeColumnCategory.class, mappedBy = "parent", fetch = FetchType.LAZY)
    @OrderBy(value = "orderNo asc")
    @Where(clause = "objectStatus = 1")
    private List<CodeColumnCategory> childList = new ArrayList<CodeColumnCategory>();

    @OneToMany(targetEntity = CodeColumn.class, mappedBy = "codeColumnCategory", fetch = FetchType.LAZY)
    @OrderBy(value = "orderNo asc")
    @Where(clause = "objectStatus = 1")
    private List<CodeColumn> codeColumnList = new ArrayList<CodeColumn>();

    //=================== 属性方法 ============================================

    public CodeColumnCategory getParent() {

        return parent;
    }

    public void setParent(CodeColumnCategory parent) {

        this.parent = parent;
    }

    public List<CodeColumnCategory> getChildList() {

        return childList;
    }

    public void setChildList(List<CodeColumnCategory> childList) {

        this.childList = childList;
    }

    public List<CodeColumn> getCodeColumnList() {

        return codeColumnList;
    }

    public void setCodeColumnList(List<CodeColumn> codeColumnList) {

        this.codeColumnList = codeColumnList;
    }
}