package com.withub.model.system.po;


import com.withub.model.entity.AbstractBaseEntity;

import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@javax.persistence.Entity
@Table(name = "SYS_USERCLUSTER")
public class UserCluster extends AbstractBaseEntity {

    //======================== 属性声明 ===============================================

    private String name;

    @ManyToOne(targetEntity = UserClusterCategory.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "UserClusterCategoryId")
    private UserClusterCategory userClusterCategory;

    private String userClusterTag;

    private Integer entityInstanceCluster;

    private Integer runtime;

    private Integer orderNo;

    //======================== 属性方法 ===============================================


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public UserClusterCategory getUserClusterCategory() {
        return userClusterCategory;
    }

    public void setUserClusterCategory(UserClusterCategory userClusterCategory) {
        this.userClusterCategory = userClusterCategory;
    }

    public String getUserClusterTag() {
        return userClusterTag;
    }

    public void setUserClusterTag(String userClusterTag) {
        this.userClusterTag = userClusterTag;
    }

    public Integer getEntityInstanceCluster() {
        return entityInstanceCluster;
    }

    public void setEntityInstanceCluster(Integer entityInstanceCluster) {
        this.entityInstanceCluster = entityInstanceCluster;
    }

    public Integer getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(Integer orderNo) {
        this.orderNo = orderNo;
    }

    public Integer getRuntime() {
        return runtime;
    }

    public void setRuntime(Integer runtime) {
        this.runtime = runtime;
    }
}