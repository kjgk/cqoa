package com.withub.model.workflow.vo;

import com.withub.model.entity.AbstractEntity;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(name = "VW_INSTANCETASKLOG")
public class InstanceTaskLog extends AbstractEntity {

    private String taskId;

    private String instanceId;

    private String relatedObjectId;

    private String flowNodeId;

    private String flowNodeName;

    private String flowNodeType;

    private String taskHandleResult;

    private String taskHandleResultName;

    private String opinion;

    private String handler;

    private String handlerName;

    private Date taskCreateTime;

    private Long taskTimeMillis;

    private Date taskFinishTime;

    //=================== 属性方法 ============================================

    public String getTaskId() {

        return taskId;
    }

    public void setTaskId(String taskId) {

        this.taskId = taskId;
    }

    public String getInstanceId() {

        return instanceId;
    }

    public void setInstanceId(String instanceId) {

        this.instanceId = instanceId;
    }

    public String getRelatedObjectId() {

        return relatedObjectId;
    }

    public void setRelatedObjectId(String relatedObjectId) {

        this.relatedObjectId = relatedObjectId;
    }

    public String getFlowNodeId() {

        return flowNodeId;
    }

    public void setFlowNodeId(String flowNodeId) {

        this.flowNodeId = flowNodeId;
    }

    public String getFlowNodeName() {

        return flowNodeName;
    }

    public void setFlowNodeName(String flowNodeName) {

        this.flowNodeName = flowNodeName;
    }

    public String getFlowNodeType() {

        return flowNodeType;
    }

    public void setFlowNodeType(String flowNodeType) {

        this.flowNodeType = flowNodeType;
    }

    public String getTaskHandleResult() {

        return taskHandleResult;
    }

    public void setTaskHandleResult(String taskHandleResult) {

        this.taskHandleResult = taskHandleResult;
    }

    public String getTaskHandleResultName() {

        return taskHandleResultName;
    }

    public void setTaskHandleResultName(String taskHandleResultName) {

        this.taskHandleResultName = taskHandleResultName;
    }

    public String getOpinion() {

        return opinion;
    }

    public void setOpinion(String opinion) {

        this.opinion = opinion;
    }

    public String getHandler() {
        return handler;
    }

    public void setHandler(String handler) {
        this.handler = handler;
    }

    public String getHandlerName() {
        return handlerName;
    }

    public void setHandlerName(String handlerName) {
        this.handlerName = handlerName;
    }

    public Date getTaskCreateTime() {

        return taskCreateTime;
    }

    public void setTaskCreateTime(Date taskCreateTime) {

        this.taskCreateTime = taskCreateTime;
    }

    public Long getTaskTimeMillis() {
        return taskTimeMillis;
    }

    public void setTaskTimeMillis(Long taskTimeMillis) {
        this.taskTimeMillis = taskTimeMillis;
    }

    public Date getTaskFinishTime() {

        return taskFinishTime;
    }

    public void setTaskFinishTime(Date taskFinishTime) {

        this.taskFinishTime = taskFinishTime;
    }
}