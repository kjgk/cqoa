package com.withub.model.system.po;


import com.withub.model.entity.AbstractBaseEntity;

import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@javax.persistence.Entity
@Table(name = "SYS_AUTHORIZATIONCLUSTERCACHE")
public class AuthorizationClusterCache extends AbstractBaseEntity {

    //======================== 属性声明 ===============================================

    @OneToOne(targetEntity = AuthorizationCluster.class)
    @JoinColumn(name = "AuthorizationClusterId")
    private AuthorizationCluster authorizationCluster;

    @OneToOne(targetEntity = User.class)
    @JoinColumn(name = "UserId")
    private User user;

    //======================== 属性方法 ===============================================

    public AuthorizationCluster getAuthorizationCluster() {

        return authorizationCluster;
    }

    public void setAuthorizationCluster(AuthorizationCluster authorizationCluster) {

        this.authorizationCluster = authorizationCluster;
    }

    public User getUser() {

        return user;
    }

    public void setUser(User user) {

        this.user = user;
    }
}