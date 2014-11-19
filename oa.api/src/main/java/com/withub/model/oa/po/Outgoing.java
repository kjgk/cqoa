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
@Table(name = "OA_OUTGOING")
public class Outgoing extends AbstractBaseEntity {

    //================================ 属性声明 ==========================================================

    private Date beginDate;

    private Date endDate;

    // 本市出差
    private Integer localCity;

    @OneToOne(targetEntity = Organization.class)
    @JoinColumn(name = "organizationId")
    private Organization organization;

    // 出差事由
    private String description;

    private String destination;

    private String driveRoute;

    @OneToOne(targetEntity = Code.class)
    @JoinColumn(name = "transportation")
    private Code transportation;

    // 是否派车
    private Integer requiredCar;

    @OneToOne(targetEntity = Code.class)
    @JoinColumn(name = "status")
    private Code status;

    @OneToOne(targetEntity = User.class)
    @JoinColumn(name = "proposer")
    private User proposer;

    private Integer duration;

    //================================ 属性方法 ==========================================================

    public Date getBeginDate() {
        return beginDate;
    }

    public void setBeginDate(Date beginDate) {
        this.beginDate = beginDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Integer getLocalCity() {
        return localCity;
    }

    public void setLocalCity(Integer localCity) {
        this.localCity = localCity;
    }

    public Organization getOrganization() {
        return organization;
    }

    public void setOrganization(Organization organization) {
        this.organization = organization;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getDriveRoute() {
        return driveRoute;
    }

    public void setDriveRoute(String driveRoute) {
        this.driveRoute = driveRoute;
    }

    public Code getTransportation() {
        return transportation;
    }

    public void setTransportation(Code transportation) {
        this.transportation = transportation;
    }

    public Integer getRequiredCar() {
        return requiredCar;
    }

    public void setRequiredCar(Integer requiredCar) {
        this.requiredCar = requiredCar;
    }

    public Code getStatus() {
        return status;
    }

    public void setStatus(Code status) {
        this.status = status;
    }

    public User getProposer() {
        return proposer;
    }

    public void setProposer(User proposer) {
        this.proposer = proposer;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }
}