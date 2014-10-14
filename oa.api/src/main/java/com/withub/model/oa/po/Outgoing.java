package com.withub.model.oa.po;

import com.withub.model.entity.AbstractBaseEntity;
import com.withub.model.system.po.Organization;

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

    // 是否为本市出差
    private Integer localCity;

    @OneToOne(targetEntity = Organization.class)
    @JoinColumn(name = "organizationId")
    private Organization organization;

    // 出差事由
    private String description;

    private String destination;

    private String driveRoute;

    private String transportation;

    private Integer requiredCar;

    private String status;

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

    public String getTransportation() {
        return transportation;
    }

    public void setTransportation(String transportation) {
        this.transportation = transportation;
    }

    public Integer getRequiredCar() {
        return requiredCar;
    }

    public void setRequiredCar(Integer requiredCar) {
        this.requiredCar = requiredCar;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}