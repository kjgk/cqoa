package com.withub.model.system.po;


import com.withub.model.entity.AbstractBaseEntity;
import org.codehaus.jackson.annotate.JsonIgnore;

import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.List;

@javax.persistence.Entity
@Table(name = "SYS_ROLE")
public class Role extends AbstractBaseEntity {

    //======================= 属性声明 ===========================================

    private String name;

    private String roleTag;

    @OneToMany(targetEntity = UserOrganizationRole.class, mappedBy = "role", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<UserOrganizationRole> roleUserList;

    private String description;

    private Integer orderNo;

    //======================= 属性方法 ===========================================

    public String getName() {

        return name;
    }

    public void setName(String name) {

        this.name = name;
    }

    public String getRoleTag() {

        return roleTag;
    }

    public void setRoleTag(String roleTag) {

        this.roleTag = roleTag;
    }

    public List<UserOrganizationRole> getRoleUserList() {

        return roleUserList;
    }

    public void setRoleUserList(List<UserOrganizationRole> roleUserList) {

        this.roleUserList = roleUserList;
    }

    public String getDescription() {

        return description;
    }

    public void setDescription(String description) {

        this.description = description;
    }

    public Integer getOrderNo() {

        return orderNo;
    }

    public void setOrderNo(Integer orderNo) {

        this.orderNo = orderNo;
    }
}