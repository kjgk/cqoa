package com.withub.model.system.po;


import com.withub.model.entity.AbstractEntity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@javax.persistence.Entity
@Table(name = "SYS_USERCLUSTERDETAIL")
public class UserClusterDetail extends AbstractEntity {

    //======================== 属性声明 ===============================================


    @OneToOne(targetEntity = Entity.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "USERCLUSTERENTITYID")
    private Entity userClusterEntity;

    private String userClusterEntityInstanceId;

    @OneToOne(targetEntity = UserClusterRegulation.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "USERCLUSTERREGULATIONID")
    private UserClusterRegulation userClusterRegulation;

    private String relatedObjectId;

    @ManyToOne(targetEntity = UserClusterDetail.class)
    @JoinColumn(name = "PARENTID")
    private UserClusterDetail parent;

    @OneToMany(targetEntity = UserClusterDetail.class, mappedBy = "parent", fetch = FetchType.LAZY)
    private List<UserClusterDetail> childList = new ArrayList<UserClusterDetail>();

    //======================== 属性方法 ===============================================

    public Entity getUserClusterEntity() {
        return userClusterEntity;
    }

    public void setUserClusterEntity(Entity userClusterEntity) {
        this.userClusterEntity = userClusterEntity;
    }

    public String getUserClusterEntityInstanceId() {
        return userClusterEntityInstanceId;
    }

    public void setUserClusterEntityInstanceId(String userClusterEntityInstanceId) {
        this.userClusterEntityInstanceId = userClusterEntityInstanceId;
    }

    public UserClusterRegulation getUserClusterRegulation() {
        return userClusterRegulation;
    }

    public void setUserClusterRegulation(UserClusterRegulation userClusterRegulation) {
        this.userClusterRegulation = userClusterRegulation;
    }

    public String getRelatedObjectId() {
        return relatedObjectId;
    }

    public void setRelatedObjectId(String relatedObjectId) {
        this.relatedObjectId = relatedObjectId;
    }

    public UserClusterDetail getParent() {
        return parent;
    }

    public void setParent(UserClusterDetail parent) {
        this.parent = parent;
    }

    public List<UserClusterDetail> getChildList() {
        return childList;
    }

    public void setChildList(List<UserClusterDetail> childList) {
        this.childList = childList;
    }
}