package com.withub.model.std.po;


import com.withub.model.entity.AbstractRecursionEntity;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@javax.persistence.Entity
@Table(name = "STD_LAWCATEGORY")
public class LawCategory extends AbstractRecursionEntity {

    //================================ 属性声明 ===========================================================

    private String code;

    @ManyToOne(targetEntity = LawCategory.class)
    @JoinColumn(name = "ParentId")
    private LawCategory parent;

    @OneToMany(targetEntity = LawCategory.class, mappedBy = "parent", fetch = FetchType.LAZY)
    @OrderBy(value = "orderNo asc")
    @Where(clause = "objectStatus = 1")
    private List<LawCategory> childList = new ArrayList<LawCategory>();

    @OneToMany(targetEntity = Law.class, mappedBy = "lawCategory", fetch = FetchType.LAZY)
    @OrderBy(value = "issueDate desc")
    @Where(clause = "objectStatus = 1")
    private List<Law> lawList;

    //================================ 属性方法 ===========================================================

    public String getCode() {

        return code;
    }

    public void setCode(String code) {

        this.code = code;
    }

    public LawCategory getParent() {

        return parent;
    }

    public void setParent(LawCategory parent) {

        this.parent = parent;
    }

    public List<LawCategory> getChildList() {

        return childList;
    }

    public void setChildList(List<LawCategory> childList) {

        this.childList = childList;
    }

    public List<Law> getLawList() {

        return lawList;
    }

    public void setLawList(List<Law> lawList) {

        this.lawList = lawList;
    }
}