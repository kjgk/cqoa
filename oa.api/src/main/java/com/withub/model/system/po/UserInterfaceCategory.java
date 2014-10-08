package com.withub.model.system.po;

import com.withub.model.entity.AbstractRecursionEntity;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@javax.persistence.Entity
@Table(name = "SYS_USERINTERFACECATEGORY")
public class UserInterfaceCategory extends AbstractRecursionEntity {

    //=================== 属性声明 ===========================================

    @ManyToOne(targetEntity = UserInterfaceCategory.class)
    @JoinColumn(name = "parentId")
    private UserInterfaceCategory parent;

    @OneToMany(targetEntity = UserInterfaceCategory.class, mappedBy = "parent", fetch = FetchType.LAZY)
    @OrderBy(value = "orderNo asc")
    @Where(clause = "objectStatus = 1")
    private List<UserInterfaceCategory> childList = new ArrayList<UserInterfaceCategory>();

    @OneToMany(targetEntity = UserInterface.class, mappedBy = "userInterfaceCategory", fetch = FetchType.LAZY)
    @OrderBy(value = "orderNo asc")
    @Where(clause = "objectStatus = 1")
    private List<UserInterface> userInterfaceList = new ArrayList<UserInterface>();

    //=================== 属性方法 ============================================


    public UserInterfaceCategory getParent() {

        return parent;
    }

    public void setParent(UserInterfaceCategory parent) {

        this.parent = parent;
    }

    public List<UserInterfaceCategory> getChildList() {

        return childList;
    }

    public void setChildList(List<UserInterfaceCategory> childList) {

        this.childList = childList;
    }

    public List<UserInterface> getUserInterfaceList() {

        return userInterfaceList;
    }

    public void setUserInterfaceList(List<UserInterface> userInterfaceList) {

        this.userInterfaceList = userInterfaceList;
    }
}