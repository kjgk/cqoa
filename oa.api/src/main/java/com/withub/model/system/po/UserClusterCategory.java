package com.withub.model.system.po;

import com.withub.model.entity.AbstractRecursionEntity;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@javax.persistence.Entity
@Table(name = "SYS_USERCLUSTERCATEGORY")
public class UserClusterCategory extends AbstractRecursionEntity {

    //=================== 属性声明 ===========================================

    @ManyToOne(targetEntity = UserClusterCategory.class)
    @JoinColumn(name = "parentId")
    private UserClusterCategory parent;

    private String cacheEntityName;

    @OneToMany(targetEntity = UserClusterCategory.class, mappedBy = "parent", fetch = FetchType.LAZY)
    @OrderBy(value = "orderNo asc")
    @Where(clause = "objectStatus = 1")
    private List<UserClusterCategory> childList = new ArrayList<UserClusterCategory>();

    @OneToMany(targetEntity = UserCluster.class, mappedBy = "userClusterCategory", fetch = FetchType.LAZY)
    @OrderBy(value = "orderNo asc")
    @Where(clause = "objectStatus = 1")
    private List<UserCluster> userClusterList = new ArrayList<UserCluster>();

    //=================== 属性方法 ============================================

    public UserClusterCategory getParent() {

        return parent;
    }

    public void setParent(UserClusterCategory parent) {

        this.parent = parent;
    }

    public String getCacheEntityName() {

        return cacheEntityName;
    }

    public void setCacheEntityName(String cacheEntityName) {

        this.cacheEntityName = cacheEntityName;
    }

    public List<UserClusterCategory> getChildList() {

        return childList;
    }

    public void setChildList(List<UserClusterCategory> childList) {

        this.childList = childList;
    }

    public List<UserCluster> getUserClusterList() {

        return userClusterList;
    }

    public void setUserClusterList(List<UserCluster> userClusterList) {

        this.userClusterList = userClusterList;
    }
}