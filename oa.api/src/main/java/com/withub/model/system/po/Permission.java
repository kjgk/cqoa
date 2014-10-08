package com.withub.model.system.po;


import com.withub.model.entity.AbstractBaseEntity;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@javax.persistence.Entity
@Table(name = "SYS_PERMISSION")
public class Permission extends AbstractBaseEntity {

    //=================================== 属性声明 ========================================================

    private String name;

    @ManyToOne(targetEntity = Entity.class)
    @JoinColumn(name = "EntityId")
    private Entity entity;

    private String serviceMethod;

    private String permissionTag;

    @OneToMany(targetEntity = PermissionRegulation.class, mappedBy = "permission", fetch = FetchType.LAZY)
    @OrderBy(value = "orderNo asc")
    @Where(clause = "objectStatus = 1 and parentId is null")
    private List<PermissionRegulation> permissionRegulationList = new ArrayList<PermissionRegulation>();

    @OneToOne(targetEntity = Menu.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "MenuId")
    private Menu menu;

    private String description;

    private Integer orderNo;

    //=================================== 属性方法 ========================================================

    public String getName() {

        return name;
    }

    public void setName(String name) {

        this.name = name;
    }

    public Entity getEntity() {

        return entity;
    }

    public void setEntity(Entity entity) {

        this.entity = entity;
    }

    public String getServiceMethod() {

        return serviceMethod;
    }

    public void setServiceMethod(String serviceMethod) {

        this.serviceMethod = serviceMethod;
    }

    public String getPermissionTag() {

        return permissionTag;
    }

    public void setPermissionTag(String permissionTag) {

        this.permissionTag = permissionTag;
    }

    public List<PermissionRegulation> getPermissionRegulationList() {

        return permissionRegulationList;
    }

    public void setPermissionRegulationList(List<PermissionRegulation> permissionRegulationList) {

        this.permissionRegulationList = permissionRegulationList;
    }

    public Menu getMenu() {

        return menu;
    }

    public void setMenu(Menu menu) {

        this.menu = menu;
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