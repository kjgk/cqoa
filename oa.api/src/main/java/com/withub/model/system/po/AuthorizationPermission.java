package com.withub.model.system.po;

import com.withub.model.entity.AbstractEntity;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.List;

@javax.persistence.Entity
@Table(name = "SYS_AUTHORIZATIONPERMISSION")
public class AuthorizationPermission extends AbstractEntity {

    //=================== 属性声明 ============================================

    @OneToOne(targetEntity = AuthorizationCluster.class)
    @JoinColumn(name = "AuthorizationClusterId")
    private AuthorizationCluster authorizationCluster;

    @OneToOne(targetEntity = Permission.class)
    @JoinColumn(name = "PermissionId")
    private Permission permission;

    @OneToMany(targetEntity = AuthorizationRegulation.class, mappedBy = "authorizationPermission", fetch = FetchType.LAZY)
    @Where(clause = "parentId is null")
    private List<AuthorizationRegulation> authorizationRegulationList;

    //=================== 属性方法 ============================================

    public AuthorizationCluster getAuthorizationCluster() {

        return authorizationCluster;
    }

    public void setAuthorizationCluster(AuthorizationCluster authorizationCluster) {

        this.authorizationCluster = authorizationCluster;
    }

    public Permission getPermission() {

        return permission;
    }

    public void setPermission(Permission permission) {

        this.permission = permission;
    }

    public List<AuthorizationRegulation> getAuthorizationRegulationList() {

        return authorizationRegulationList;
    }

    public void setAuthorizationRegulationList(List<AuthorizationRegulation> authorizationRegulationList) {

        this.authorizationRegulationList = authorizationRegulationList;
    }
}