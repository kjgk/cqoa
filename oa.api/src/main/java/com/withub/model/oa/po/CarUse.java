package com.withub.model.oa.po;

import com.withub.model.entity.AbstractBaseEntity;
import com.withub.model.system.po.Code;
import com.withub.model.system.po.Organization;
import com.withub.model.system.po.User;
import org.codehaus.jackson.annotate.JsonIgnore;

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

    @OneToOne(targetEntity = Code.class)
    @JoinColumn(name = "status")
    private Code status;

    @OneToOne(targetEntity = User.class)
    @JoinColumn(name = "proposer")
    private User proposer;

    //================================ 属性方法 ==========================================================


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

    public Integer getLocalCity() {
        return localCity;
    }

    public void setLocalCity(Integer localCity) {
        this.localCity = localCity;
    }

    public List<CarUseInfo> getCarUseInfoList() {
        return carUseInfoList;
    }

    public void setCarUseInfoList(List<CarUseInfo> carUseInfoList) {
        this.carUseInfoList = carUseInfoList;
    }

    public Organization getOrganization() {
        return organization;
    }

    public void setOrganization(Organization organization) {
        this.organization = organization;
    }

    public Outgoing getOutgoing() {
        return outgoing;
    }

    public void setOutgoing(Outgoing outgoing) {
        this.outgoing = outgoing;
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
}