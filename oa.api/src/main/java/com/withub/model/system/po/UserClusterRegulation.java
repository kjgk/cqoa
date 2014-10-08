package com.withub.model.system.po;

import com.withub.model.entity.AbstractRecursionEntity;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@javax.persistence.Entity
@Table(name = "SYS_USERCLUSTERREGULATION")
public class UserClusterRegulation extends AbstractRecursionEntity {

    //=================== 属性声明 ===========================================

    @OneToOne(targetEntity = UserCluster.class, fetch = FetchType.EAGER)
    @JoinColumn(name = "userClusterId")
    private UserCluster userCluster;

    @ManyToOne(targetEntity = UserClusterRegulation.class)
    @JoinColumn(name = "parentId")
    private UserClusterRegulation parent;

    @OneToMany(targetEntity = UserClusterRegulation.class, mappedBy = "parent", fetch = FetchType.LAZY)
    @OrderBy(value = "orderNo asc")
    @Where(clause = "objectStatus = 1")
    private List<UserClusterRegulation> childList = new ArrayList<UserClusterRegulation>();

    @OneToOne(targetEntity = Entity.class, fetch = FetchType.EAGER)
    @JoinColumn(name = "entityId")
    private Entity entity;

    private String codeColumnTag;

    private String userProperty;

    private String extandedRoleProperty;

    @OneToOne(targetEntity = Entity.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "extandedPropertyEntityId")
    private Entity extandedPropertyEntity;

    private String extandedPropertyCodeColumnTag;

    //=================== 属性方法 ============================================


    public UserClusterRegulation getParent() {
        return parent;
    }

    public void setParent(UserClusterRegulation parent) {
        this.parent = parent;
    }

    public List<UserClusterRegulation> getChildList() {
        return childList;
    }

    public void setChildList(List<UserClusterRegulation> childList) {
        this.childList = childList;
    }

    public Entity getEntity() {
        return entity;
    }

    public void setEntity(Entity entity) {
        this.entity = entity;
    }

    public String getCodeColumnTag() {
        return codeColumnTag;
    }

    public void setCodeColumnTag(String codeColumnTag) {
        this.codeColumnTag = codeColumnTag;
    }

    public String getUserProperty() {
        return userProperty;
    }

    public void setUserProperty(String userProperty) {
        this.userProperty = userProperty;
    }

    public String getExtandedRoleProperty() {
        return extandedRoleProperty;
    }

    public void setExtandedRoleProperty(String extandedRoleProperty) {
        this.extandedRoleProperty = extandedRoleProperty;
    }

    public Entity getExtandedPropertyEntity() {
        return extandedPropertyEntity;
    }

    public void setExtandedPropertyEntity(Entity extandedPropertyEntity) {
        this.extandedPropertyEntity = extandedPropertyEntity;
    }

    public String getExtandedPropertyCodeColumnTag() {
        return extandedPropertyCodeColumnTag;
    }

    public void setExtandedPropertyCodeColumnTag(String extandedPropertyCodeColumnTag) {
        this.extandedPropertyCodeColumnTag = extandedPropertyCodeColumnTag;
    }

    public UserCluster getUserCluster() {
        return userCluster;
    }

    public void setUserCluster(UserCluster userCluster) {
        this.userCluster = userCluster;
    }
}