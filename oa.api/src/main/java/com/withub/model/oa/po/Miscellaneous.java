package com.withub.model.oa.po;

import com.withub.model.entity.AbstractBaseEntity;
import com.withub.model.system.po.Code;
import com.withub.model.system.po.Organization;
import com.withub.model.system.po.User;

import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.util.Date;

@javax.persistence.Entity
@Table(name = "OA_MISCELLANEOUS")
public class Miscellaneous extends AbstractBaseEntity {

    //================================ 属性声明 ==========================================================

    @OneToOne(targetEntity = Organization.class)
    @JoinColumn(name = "organizationId")
    private Organization organization;

    @OneToOne(targetEntity = Code.class)
    @JoinColumn(name = "status")
    private Code status;

    // 申请事由
    private String description;

    @OneToOne(targetEntity = User.class)
    @JoinColumn(name = "proposer")
    private User proposer;

    //================================ 属性方法 ==========================================================

    public Organization getOrganization() {
        return organization;
    }

    public void setOrganization(Organization organization) {
        this.organization = organization;
    }

    public Code getStatus() {
        return status;
    }

    public void setStatus(Code status) {
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public User getProposer() {
        return proposer;
    }

    public void setProposer(User proposer) {
        this.proposer = proposer;
    }
}