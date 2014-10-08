package com.withub.model.std.po;

import com.withub.model.entity.AbstractRecursionEntity;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "STD_CUSTOMPAGECATEGORY")
public class CustomPageCategory extends AbstractRecursionEntity {

    //=================== 属性声明 ============================================

    @ManyToOne(targetEntity = CustomPageCategory.class)
    @JoinColumn(name = "ParentId")
    private CustomPageCategory parent;

    @OneToMany(targetEntity = CustomPageCategory.class, mappedBy = "parent", fetch = FetchType.LAZY)
    @OrderBy(value = "orderNo asc")
    @Where(clause = "objectStatus = 1")
    private List<CustomPageCategory> childList = new ArrayList<CustomPageCategory>();

    @OneToMany(targetEntity = CustomPage.class, mappedBy = "customPageCategory", fetch = FetchType.LAZY)
    @OrderBy(value = "orderNo asc")
    @Where(clause = "objectStatus = 1")
    private List<CustomPage> customPageList;

    //=================== 属性方法 ============================================

    public CustomPageCategory getParent() {
        return parent;
    }

    public void setParent(CustomPageCategory parent) {
        this.parent = parent;
    }

    public List<CustomPageCategory> getChildList() {
        return childList;
    }

    public void setChildList(List<CustomPageCategory> childList) {
        this.childList = childList;
    }

    public List<CustomPage> getCustomPageList() {
        return customPageList;
    }

    public void setCustomPageList(List<CustomPage> customPageList) {
        this.customPageList = customPageList;
    }
}