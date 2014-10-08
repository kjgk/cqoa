package com.withub.model.workflow.po;

import com.withub.model.entity.AbstractEntity;
import com.withub.model.system.po.Code;
import com.withub.model.system.po.User;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Entity
@Table(name = "WF_MASTERTASK")
public class MasterTask extends AbstractEntity {

    @ManyToOne(targetEntity = SubInstance.class)
    @JoinColumn(name = "subInstanceId")
    private SubInstance subInstance;

    @OneToOne(targetEntity = MasterTask.class)
    @JoinColumn(name = "previousMasterTaskId")
    private MasterTask previousMasterTask;

    @OneToMany(targetEntity = Task.class, mappedBy = "masterTask", fetch = FetchType.LAZY)
    @OrderBy(value = "createTime asc")
    private List<Task> taskList = new ArrayList<Task>();

    @OneToOne(targetEntity = FlowNode.class)
    @JoinColumn(name = "flowNodeId")
    private FlowNode flowNode;

    @OneToOne(targetEntity = User.class)
    @JoinColumn(name = "starter")
    private User starter;

    @OneToOne(targetEntity = Code.class)
    @JoinColumn(name = "status")
    private Code status;

    private Date createTime;

    private Date finishTime;

    //=================== 属性方法 ============================================

    public SubInstance getSubInstance() {

        return subInstance;
    }

    public void setSubInstance(SubInstance subInstance) {

        this.subInstance = subInstance;
    }

    public MasterTask getPreviousMasterTask() {

        return previousMasterTask;
    }

    public void setPreviousMasterTask(MasterTask previousMasterTask) {

        this.previousMasterTask = previousMasterTask;
    }

    public List<Task> getTaskList() {

        return taskList;
    }

    public void setTaskList(List<Task> taskList) {

        this.taskList = taskList;
    }

    public FlowNode getFlowNode() {

        return flowNode;
    }

    public void setFlowNode(FlowNode flowNode) {

        this.flowNode = flowNode;
    }

    public User getStarter() {

        return starter;
    }

    public void setStarter(User starter) {

        this.starter = starter;
    }

    public Code getStatus() {

        return status;
    }

    public void setStatus(Code status) {

        this.status = status;
    }

    public Date getCreateTime() {

        return createTime;
    }

    public void setCreateTime(Date createTime) {

        this.createTime = createTime;
    }

    public Date getFinishTime() {

        return finishTime;
    }

    public void setFinishTime(Date finishTime) {

        this.finishTime = finishTime;
    }
}