package com.withub.model.system.po;


import com.withub.model.entity.AbstractBaseEntity;

import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.util.Date;

@javax.persistence.Entity
@Table(name = "SYS_USERORGANIZATIONHISTORY")
public class UserOrganizationHistory extends AbstractBaseEntity {

    //==================== 属性声明 ============================================

    @OneToOne(targetEntity = User.class)
    @JoinColumn(name = "UserId")
    private User user;

    @OneToOne(targetEntity = Organization.class)
    @JoinColumn(name = "OrganizationId")
    private Organization organization;

    @OneToOne(targetEntity = Role.class)
    @JoinColumn(name = "RoleId")
    private Role role;

    private Date enterDate;

    private Date leaveDate;

    private Integer status;

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

    public Date getEnterDate() {

        return enterDate;
    }

    public void setEnterDate(Date enterDate) {

        this.enterDate = enterDate;
    }

    public Date getLeaveDate() {

        return leaveDate;
    }

    public void setLeaveDate(Date leaveDate) {

        this.leaveDate = leaveDate;
    }

    public Integer getStatus() {

        return status;
    }

    public void setStatus(Integer status) {

        this.status = status;
    }
}