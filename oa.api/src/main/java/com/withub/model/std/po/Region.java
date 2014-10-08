package com.withub.model.std.po;


import com.withub.model.entity.AbstractRecursionEntity;
import com.withub.model.system.po.Code;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "STD_REGION")
public class Region extends AbstractRecursionEntity {

    //======================= 属性声明 ===============================================

    private String code;

    @OneToOne(targetEntity = Code.class)
    @JoinColumn(name = "RegionTypeId")
    private Code regionType;

    @ManyToOne(targetEntity = Region.class)
    @JoinColumn(name = "parentId")
    private Region parent;

    @OneToMany(targetEntity = Region.class, mappedBy = "parent", fetch = FetchType.LAZY)
    @OrderBy(value = "orderNo asc")
    @Where(clause = "objectStatus = 1")
    private List<Region> childList = new ArrayList<Region>();

    //======================= 属性方法 ===============================================

    public String getCode() {

        return code;
    }

    public void setCode(String code) {

        this.code = code;
    }

    public Code getRegionType() {

        return regionType;
    }

    public void setRegionType(Code regionType) {

        this.regionType = regionType;
    }

    public Region getParent() {

        return parent;
    }

    public void setParent(Region parent) {

        this.parent = parent;
    }

    public List<Region> getChildList() {

        return childList;
    }

    public void setChildList(List<Region> childList) {

        this.childList = childList;
    }
}