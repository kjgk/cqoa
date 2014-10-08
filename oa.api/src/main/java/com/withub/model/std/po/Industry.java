package com.withub.model.std.po;


import com.withub.model.entity.AbstractRecursionEntity;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "STD_INDUSTRY")
public class Industry extends AbstractRecursionEntity {

    //================== 属性声明 ===============================================

    private String code;

    @ManyToOne(targetEntity = Industry.class)
    @JoinColumn(name = "parentId")
    private Industry parent;

    @OneToMany(targetEntity = Industry.class, mappedBy = "parent", fetch = FetchType.LAZY)
    @OrderBy(value = "orderNo asc")
    @Where(clause = "objectStatus = 1")
    private List<Industry> childList = new ArrayList<Industry>();

    //================== 属性方法 ===============================================

    public String getCode() {

        return code;
    }

    public void setCode(String code) {

        this.code = code;
    }

    public Industry getParent() {

        return parent;
    }

    public void setParent(Industry parent) {

        this.parent = parent;
    }

    public List<Industry> getChildList() {

        return childList;
    }

    public void setChildList(List<Industry> childList) {

        this.childList = childList;
    }
}
