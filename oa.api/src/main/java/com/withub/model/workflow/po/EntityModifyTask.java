package com.withub.model.workflow.po;

import com.withub.model.entity.AbstractEntity;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "WF_ENTITYMODIFYTASK")
public class EntityModifyTask extends AbstractEntity {

    //============================= 属性声明 ============================================

    private String entityInstanceId;

    @OneToOne(targetEntity = Task.class)
    @JoinColumn(name = "taskId")
    private Task task;

    @OneToOne(targetEntity = FlowNode.class)
    @JoinColumn(name = "FlowNodeId")
    private FlowNode flowNode;

    //============================= 属性方法 ============================================

    public String getEntityInstanceId() {

        return entityInstanceId;
    }

    public void setEntityInstanceId(String entityInstanceId) {

        this.entityInstanceId = entityInstanceId;
    }

    public Task getTask() {

        return task;
    }

    public void setTask(Task task) {

        this.task = task;
    }

    public FlowNode getFlowNode() {

        return flowNode;
    }

    public void setFlowNode(FlowNode flowNode) {

        this.flowNode = flowNode;
    }

}
