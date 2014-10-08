package com.withub.model.workflow.po;

import com.withub.model.entity.AbstractBaseEntity;
import com.withub.model.system.po.Code;
import com.withub.model.system.po.Organization;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@javax.persistence.Entity
@Table(name = "WF_INSTANCE")
public class Instance extends AbstractBaseEntity {

    //============================== 属性声明 ========================================================

    private String name;

    @OneToOne(targetEntity = FlowType.class)
    @JoinColumn(name = "flowTypeId")
    private FlowType flowType;

    private String relatedObjectId;

    @OneToOne(targetEntity = FlowNode.class)
    @JoinColumn(name = "CurrentFlowNodeId")
    private FlowNode currentFlowNode;

    @OneToMany(targetEntity = InstanceOrganization.class, mappedBy = "instance", fetch = FetchType.LAZY)
    private List<InstanceOrganization> organizationList;

    @OneToMany(targetEntity = SubInstance.class, mappedBy = "instance", fetch = FetchType.LAZY)
    @OrderBy(value = "createTime desc")
    private List<SubInstance> subInstanceList;

    @OneToOne(targetEntity = Organization.class)
    @JoinColumn(name = "OrganizationId")
    private Organization organization;

    @OneToOne(targetEntity = Code.class)
    @JoinColumn(name = "status")
    private Code status;

    private Date finishTime;

    @OneToOne(targetEntity = Code.class)
    @JoinColumn(name = "result")
    private Code result;

    //============================== 属性方法 ========================================================

    public String getName() {

        return name;
    }

    public void setName(String name) {

        this.name = name;
    }

    public FlowType getFlowType() {

        return flowType;
    }

    public void setFlowType(FlowType flowType) {

        this.flowType = flowType;
    }

    public String getRelatedObjectId() {

        return relatedObjectId;
    }

    public void setRelatedObjectId(String relatedObjectId) {

        this.relatedObjectId = relatedObjectId;
    }

    public FlowNode getCurrentFlowNode() {

        return currentFlowNode;
    }

    public void setCurrentFlowNode(FlowNode currentFlowNode) {

        this.currentFlowNode = currentFlowNode;
    }

    public List<InstanceOrganization> getOrganizationList() {

        return organizationList;
    }

    public void setOrganizationList(List<InstanceOrganization> organizationList) {

        this.organizationList = organizationList;
    }

    public List<SubInstance> getSubInstanceList() {

        return subInstanceList;
    }

    public void setSubInstanceList(List<SubInstance> subInstanceList) {

        this.subInstanceList = subInstanceList;
    }

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

    public Date getFinishTime() {

        return finishTime;
    }

    public void setFinishTime(Date finishTime) {

        this.finishTime = finishTime;
    }

    public Code getResult() {

        return result;
    }

    public void setResult(Code result) {

        this.result = result;
    }

}