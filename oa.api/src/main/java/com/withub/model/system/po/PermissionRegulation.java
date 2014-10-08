package com.withub.model.system.po;

import com.withub.model.entity.AbstractRecursionEntity;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;


@javax.persistence.Entity
@Table(name = "SYS_PERMISSIONREGULATION")
public class PermissionRegulation extends AbstractRecursionEntity {

    //=============== 属性声明 =======================================================

    @ManyToOne(targetEntity = Permission.class)
    @JoinColumn(name = "PermissionId")
    private Permission permission;

    private String entityProperty;

    private String userProperty;

    private String entityPropertyValue;

    @OneToOne(targetEntity = Code.class)
    @JoinColumn(name = "PropertyDataType")
    private Code propertyDataType;

    @OneToOne(targetEntity = Code.class)
    @JoinColumn(name = "ExpressionOperation")
    private Code expressionOperation;

    @ManyToOne(targetEntity = PermissionRegulation.class)
    @JoinColumn(name = "ParentId")
    private PermissionRegulation parent;

    private String regulation;

    @OneToMany(targetEntity = PermissionRegulation.class, mappedBy = "parent", fetch = FetchType.LAZY)
    @OrderBy(value = "orderNo asc")
    @Where(clause = "objectStatus = 1")
    private List<PermissionRegulation> childList = new ArrayList<PermissionRegulation>();

    //=============== 属性方法 =======================================================


    public Permission getPermission() {

        return permission;
    }

    public void setPermission(Permission permission) {

        this.permission = permission;
    }

    public String getEntityProperty() {

        return entityProperty;
    }

    public void setEntityProperty(String entityProperty) {

        this.entityProperty = entityProperty;
    }

    public String getUserProperty() {

        return userProperty;
    }

    public void setUserProperty(String userProperty) {

        this.userProperty = userProperty;
    }

    public String getEntityPropertyValue() {

        return entityPropertyValue;
    }

    public void setEntityPropertyValue(String entityPropertyValue) {

        this.entityPropertyValue = entityPropertyValue;
    }

    public Code getPropertyDataType() {

        return propertyDataType;
    }

    public void setPropertyDataType(Code propertyDataType) {

        this.propertyDataType = propertyDataType;
    }

    public Code getExpressionOperation() {

        return expressionOperation;
    }

    public void setExpressionOperation(Code expressionOperation) {

        this.expressionOperation = expressionOperation;
    }

    public PermissionRegulation getParent() {

        return parent;
    }

    public void setParent(PermissionRegulation parent) {

        this.parent = parent;
    }

    public String getRegulation() {

        return regulation;
    }

    public void setRegulation(String regulation) {

        this.regulation = regulation;
    }

    public List<PermissionRegulation> getChildList() {

        return childList;
    }

    public void setChildList(List<PermissionRegulation> childList) {

        this.childList = childList;
    }
}