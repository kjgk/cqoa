package com.withub.model.oa.po;

import com.withub.model.entity.AbstractBaseEntity;
import com.withub.model.system.po.Organization;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@javax.persistence.Entity
@Table(name = "OA_CARUSE")
public class CarUse extends AbstractBaseEntity {

    //================================ 属性声明 ==========================================================

    // 用车事由
    private String description;

    private Date beginTime;

    private Date endTime;

    private String address;

    private Integer localCity;

    @OneToMany(targetEntity = CarUseInfo.class, mappedBy = "carUse", fetch = FetchType.LAZY)
    private List<CarUseInfo> carUseInfoList;

    // 用车部门
    @OneToOne(targetEntity = Organization.class)
    @JoinColumn(name = "organizationId")
    private Organization organization;

    @OneToOne(targetEntity = Outgoing.class)
    @JoinColumn(name = "outgoingId")
    private Outgoing outgoing;

    private String status;

    //================================ 属性方法 ==========================================================


    public Integer getLocalCity() {
        return localCity;
    }

    public void setLocalCity(Integer localCity) {
        this.localCity = localCity;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(Date beginTime) {
        this.beginTime = beginTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Organization getOrganization() {
        return organization;
    }

    public void setOrganization(Organization organization) {
        this.organization = organization;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}