package com.withub.model.workflow.po;


import com.withub.model.entity.AbstractEntity;
import com.withub.model.system.po.Organization;
import com.withub.model.system.po.User;

import javax.persistence.*;
import java.util.Date;


@Entity
@Table(name = "WF_INSTANCEORGANIZATION")
public class InstanceOrganization extends AbstractEntity {

    //========================== 属性声明 ==============================================

    @ManyToOne(targetEntity = Instance.class)
    @JoinColumn(name = "instanceId")
    private Instance instance;

    @OneToOne(targetEntity = FlowNode.class)
    @JoinColumn(name = "FlowNodeId")
    private FlowNode flowNode;

    @OneToOne(targetEntity = Organization.class)
    @JoinColumn(name = "OrganizationId")
    private Organization organization;

    @OneToOne(targetEntity = User.class)
    @JoinColumn(name = "UserId")
    private User user;

    @OneToOne(targetEntity = Task.class)
    @JoinColumn(name = "TaskId")
    private Task task;

    private Date createTime;

    //========================== 属性方法 ==============================================

    public Instance getInstance() {

        return instance;
    }

    public void setInstance(Instance instance) {

        this.instance = instance;
    }

    public FlowNode getFlowNode() {

        return flowNode;
    }

    public void setFlowNode(FlowNode flowNode) {

        this.flowNode = flowNode;
    }

    public Organization getOrganization() {

        return organization;
    }

    public void setOrganization(Organization organization) {

        this.organization = organization;
    }

    public User getUser() {

        return user;
    }

    public void setUser(User user) {

        this.user = user;
    }

    public Task getTask() {

        return task;
    }

    public void setTask(Task task) {

        this.task = task;
    }

    public Date getCreateTime() {

        return createTime;
    }

    public void setCreateTime(Date createTime) {

        this.createTime = createTime;
    }
}