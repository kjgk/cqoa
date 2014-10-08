package com.withub.model.system.po;

import com.withub.model.entity.AbstractEntity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@javax.persistence.Entity
@Table(name = "SYS_AUTHORIZATIONREGULATION")
public class AuthorizationRegulation extends AbstractEntity {

    //=================== 属性声明 ============================================

    @ManyToOne(targetEntity = AuthorizationPermission.class)
    @JoinColumn(name = "AuthorizationPermissionId")
    private AuthorizationPermission authorizationPermission;

    @OneToOne(targetEntity = PermissionRegulation.class)
    @JoinColumn(name = "PermissionRegulationId")
    private PermissionRegulation permissionRegulation;

    @ManyToOne(targetEntity = AuthorizationRegulation.class)
    @JoinColumn(name = "ParentId")
    private AuthorizationRegulation parent;

    @OneToMany(targetEntity = AuthorizationRegulation.class, mappedBy = "parent", fetch = FetchType.LAZY)
    private List<AuthorizationRegulation> childList = new ArrayList<AuthorizationRegulation>();

    //=================== 属性方法 ============================================

    public AuthorizationPermission getAuthorizationPermission() {

        return authorizationPermission;
    }

    public void setAuthorizationPermission(AuthorizationPermission authorizationPermission) {

        this.authorizationPermission = authorizationPermission;
    }

    public PermissionRegulation getPermissionRegulation() {

        return permissionRegulation;
    }

    public void setPermissionRegulation(PermissionRegulation permissionRegulation) {

        this.permissionRegulation = permissionRegulation;
    }

    public AuthorizationRegulation getParent() {

        return parent;
    }

    public void setParent(AuthorizationRegulation parent) {

        this.parent = parent;
    }

    public List<AuthorizationRegulation> getChildList() {

        return childList;
    }

    public void setChildList(List<AuthorizationRegulation> childList) {

        this.childList = childList;
    }
}