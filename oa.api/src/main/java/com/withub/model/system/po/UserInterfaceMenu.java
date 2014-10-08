package com.withub.model.system.po;


import com.withub.model.entity.AbstractBaseEntity;

import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;


@javax.persistence.Entity
@Table(name = "SYS_USERINTERFACEMENU")
public class UserInterfaceMenu extends AbstractBaseEntity {

    //===================== 属性声明 =================================================

    @ManyToOne(targetEntity = UserInterface.class)
    @JoinColumn(name = "UserInterfaceId")
    private UserInterface userInterface;

    @OneToOne(targetEntity = Permission.class)
    @JoinColumn(name = "PermissionId")
    private Permission permission;

    private Integer menuType;

    private String menuTag;

    private String name;

    private String icon;

    private Integer orderNo;

    //===================== 属性方法 =================================================

    public UserInterface getUserInterface() {

        return userInterface;
    }

    public void setUserInterface(UserInterface userInterface) {

        this.userInterface = userInterface;
    }

    public Permission getPermission() {

        return permission;
    }

    public void setPermission(Permission permission) {

        this.permission = permission;
    }

    public Integer getMenuType() {

        return menuType;
    }

    public void setMenuType(Integer menuType) {

        this.menuType = menuType;
    }

    public String getMenuTag() {

        return menuTag;
    }

    public void setMenuTag(String menuTag) {

        this.menuTag = menuTag;
    }

    public String getName() {

        return name;
    }

    public void setName(String name) {

        this.name = name;
    }

    public String getIcon() {

        return icon;
    }

    public void setIcon(String icon) {

        this.icon = icon;
    }

    public Integer getOrderNo() {

        return orderNo;
    }

    public void setOrderNo(Integer orderNo) {

        this.orderNo = orderNo;
    }
}