package com.withub.model.system.po;


import com.withub.model.entity.AbstractEntity;

import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@javax.persistence.Entity
@Table(name = "SYS_AUTHORIZATIONCLUSTERMENU")
public class AuthorizationClusterMenu extends AbstractEntity {

    //======================== 属性声明 ===============================================

    @OneToOne(targetEntity = AuthorizationCluster.class)
    @JoinColumn(name = "AuthorizationClusterId")
    private AuthorizationCluster authorizationCluster;

    @OneToOne(targetEntity = Menu.class)
    @JoinColumn(name = "MenuId")
    private Menu menu;

    //======================== 属性方法 ===============================================

    public AuthorizationCluster getAuthorizationCluster() {

        return authorizationCluster;
    }

    public void setAuthorizationCluster(AuthorizationCluster authorizationCluster) {

        this.authorizationCluster = authorizationCluster;
    }

    public Menu getMenu() {

        return menu;
    }

    public void setMenu(Menu menu) {

        this.menu = menu;
    }
}