package com.withub.model.system;

import com.withub.model.entity.AbstractBaseEntity;
import com.withub.model.system.po.Organization;
import com.withub.model.system.po.UserClusterCategory;
import com.withub.model.system.po.UserClusterDetail;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@MappedSuperclass
public abstract class AbstractUserCluster extends AbstractBaseEntity {

    //============================= 属性声明 ==============================================================

    @OneToOne(targetEntity = UserClusterCategory.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "UserClusterCategoryId")
    private UserClusterCategory userClusterCategory;

    private String name;

    @OneToOne(targetEntity = Organization.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "OrganizationId")
    private Organization organization;

    private Integer allowRepetition;

    private Integer priority;

    private String description;

    private Integer enable;

    private Integer orderNo;

    @OneToMany(targetEntity = UserClusterDetail.class, mappedBy = "userClusterEntityInstanceId", fetch = FetchType.LAZY)
    private List<UserClusterDetail> userClusterDetailList = new ArrayList<UserClusterDetail>();

    //============================= 属性方法 ==============================================================

    public UserClusterCategory getUserClusterCategory() {

        return userClusterCategory;
    }

    public void setUserClusterCategory(UserClusterCategory userClusterCategory) {

        this.userClusterCategory = userClusterCategory;
    }

    public String getName() {

        return name;
    }

    public void setName(String name) {

        this.name = name;
    }

    public Organization getOrganization() {

        return organization;
    }

    public void setOrganization(Organization organization) {

        this.organization = organization;
    }

    public Integer getAllowRepetition() {

        return allowRepetition;
    }

    public void setAllowRepetition(Integer allowRepetition) {

        this.allowRepetition = allowRepetition;
    }

    public Integer getPriority() {

        return priority;
    }

    public void setPriority(Integer priority) {

        this.priority = priority;
    }

    public String getDescription() {

        return description;
    }

    public void setDescription(String description) {

        this.description = description;
    }

    public Integer getEnable() {

        return enable;
    }

    public void setEnable(Integer enable) {

        this.enable = enable;
    }

    public Integer getOrderNo() {

        return orderNo;
    }

    public void setOrderNo(Integer orderNo) {

        this.orderNo = orderNo;
    }

    public List<UserClusterDetail> getUserClusterDetailList() {

        return userClusterDetailList;
    }

    public void setUserClusterDetailList(List<UserClusterDetail> userClusterDetailList) {

        this.userClusterDetailList = userClusterDetailList;
    }
}
