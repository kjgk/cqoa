package com.withub.model.system.po;


import com.withub.model.entity.AbstractBaseEntity;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.List;

@javax.persistence.Entity
@Table(name = "SYS_USERINTERFACE")
public class UserInterface extends AbstractBaseEntity {

    //======================== 属性声明 ===============================================

    private String name;

    private String userInterfaceTag;

    @ManyToOne(targetEntity = UserInterfaceCategory.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "UserInterfaceCategoryId")
    private UserInterfaceCategory userInterfaceCategory;

    @OneToMany(targetEntity = UserInterfaceMenu.class, mappedBy = "userInterface", fetch = FetchType.LAZY)
    @Where(clause = "objectStatus=1")
    @OrderBy(" orderNo asc")
    private List<UserInterfaceMenu> menuList;

    @OneToMany(targetEntity = UserInterfaceMenu.class, mappedBy = "userInterface", fetch = FetchType.LAZY)
    @Where(clause = "menuType = 2 and objectStatus=1")
    private List<UserInterfaceMenu> toolbarMenuList;

    @Where(clause = "menuType = 1 and objectStatus=1")
    @OneToMany(targetEntity = UserInterfaceMenu.class, mappedBy = "userInterface", fetch = FetchType.LAZY)
    private List<UserInterfaceMenu> contextMenuList;

    private Integer orderNo;

    //======================== 属性方法 ===============================================

    public String getName() {

        return name;
    }

    public void setName(String name) {

        this.name = name;
    }

    public String getUserInterfaceTag() {

        return userInterfaceTag;
    }

    public void setUserInterfaceTag(String userInterfaceTag) {

        this.userInterfaceTag = userInterfaceTag;
    }

    public UserInterfaceCategory getUserInterfaceCategory() {

        return userInterfaceCategory;
    }

    public void setUserInterfaceCategory(UserInterfaceCategory userInterfaceCategory) {

        this.userInterfaceCategory = userInterfaceCategory;
    }

    public List<UserInterfaceMenu> getMenuList() {

        return menuList;
    }

    public void setMenuList(List<UserInterfaceMenu> menuList) {

        this.menuList = menuList;
    }

    public List<UserInterfaceMenu> getToolbarMenuList() {

        return toolbarMenuList;
    }

    public void setToolbarMenuList(List<UserInterfaceMenu> toolbarMenuList) {

        this.toolbarMenuList = toolbarMenuList;
    }

    public List<UserInterfaceMenu> getContextMenuList() {

        return contextMenuList;
    }

    public void setContextMenuList(List<UserInterfaceMenu> contextMenuList) {

        this.contextMenuList = contextMenuList;
    }

    public Integer getOrderNo() {

        return orderNo;
    }

    public void setOrderNo(Integer orderNo) {

        this.orderNo = orderNo;
    }
}