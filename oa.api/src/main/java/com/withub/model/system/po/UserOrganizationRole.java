package com.withub.model.system.po;


import com.withub.model.entity.AbstractEntity;

import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@javax.persistence.Entity
@Table(name = "SYS_USERORGANIZATIONROLE")
public class UserOrganizationRole extends AbstractEntity {

    //==================== 属性声明 ============================================

    @ManyToOne(targetEntity = User.class)
    @JoinColumn(name = "UserId")
    private User user;

    @OneToOne(targetEntity = Organization.class)
    @JoinColumn(name = "OrganizationId")
    private Organization organization;

    @ManyToOne(targetEntity = Role.class)
    @JoinColumn(name = "RoleId")
    private Role role;

    private Integer orderNo;

    //==================== 属性方法 ================================================

    public User getUser() {

        return user;
    }

    public void setUser(User user) {

        this.user = user;
    }

    public Organization getOrganization() {

        return organization;
    }

    public void setOrganization(Organization organization) {

        this.organization = organization;
    }

    public Role getRole() {

        return role;
    }

    public void setRole(Role role) {

        this.role = role;
    }

    public Integer getOrderNo() {

        return orderNo;
    }

    public void setOrderNo(Integer orderNo) {

        this.orderNo = orderNo;
    }
}