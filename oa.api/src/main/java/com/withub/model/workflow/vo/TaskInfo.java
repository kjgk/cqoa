package com.withub.model.workflow.vo;

import com.withub.model.entity.AbstractEntity;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(name = "VW_TASKINFO")
public class TaskInfo extends AbstractEntity {

    private String taskStatus;

    private String taskStatusName;

    private String taskStatusTag;

    private String instanceId;

    private String instanceName;

    private String relatedObjectId;

    private String owner;

    private String handler;

    private Date taskCreateTime;

    private Date taskFinishTime;

    private String taskHandleResult;

    private String taskHandleResultName;

    private String flowNodeId;

    private String flowNodeType;

    private String flowNodeName;

    private String flowTypeId;

    private String flowTypeName;

    private Integer manualSelectHandler;

    private Integer passAction;

    private Integer returnAction;

    private Integer rejectAction;

    private Integer discardAction;

    private Integer completeAction;

    private String activity;

    private String creatorName;

    private String organizationName;

    private Date taskArriveTime;

    private Integer taskSourceType;

    private Integer taskExpiration;

    private Integer taskTimeout;

    private Integer taskLocked;

    //=================== 属性方法 ============================================

    public String getTaskStatus() {

        return taskStatus;
    }

    public void setTaskStatus(String taskStatus) {

        this.taskStatus = taskStatus;
    }

    public String getTaskStatusName() {

        return taskStatusName;
    }

    public void setTaskStatusName(String taskStatusName) {

        this.taskStatusName = taskStatusName;
    }

    public String getTaskStatusTag() {

        return taskStatusTag;
    }

    public void setTaskStatusTag(String taskStatusTag) {

        this.taskStatusTag = taskStatusTag;
    }

    public String getInstanceId() {

        return instanceId;
    }

    public void setInstanceId(String instanceId) {

        this.instanceId = instanceId;
    }

    public String getInstanceName() {

        return instanceName;
    }

    public void setInstanceName(String instanceName) {

        this.instanceName = instanceName;
    }

    public String getRelatedObjectId() {

        return relatedObjectId;
    }

    public void setRelatedObjectId(String relatedObjectId) {

        this.relatedObjectId = relatedObjectId;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getHandler() {
        return handler;
    }

    public void setHandler(String handler) {
        this.handler = handler;
    }

    public Date getTaskCreateTime() {

        return taskCreateTime;
    }

    public void setTaskCreateTime(Date taskCreateTime) {

        this.taskCreateTime = taskCreateTime;
    }

    public Date getTaskFinishTime() {

        return taskFinishTime;
    }

    public void setTaskFinishTime(Date taskFinishTime) {

        this.taskFinishTime = taskFinishTime;
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

    public String getFlowNodeId() {

        return flowNodeId;
    }

    public void setFlowNodeId(String flowNodeId) {

        this.flowNodeId = flowNodeId;
    }

    public String getFlowNodeType() {

        return flowNodeType;
    }

    public void setFlowNodeType(String flowNodeType) {

        this.flowNodeType = flowNodeType;
    }

    public String getFlowNodeName() {

        return flowNodeName;
    }

    public void setFlowNodeName(String flowNodeName) {

        this.flowNodeName = flowNodeName;
    }

    public String getFlowTypeId() {

        return flowTypeId;
    }

    public void setFlowTypeId(String flowTypeId) {

        this.flowTypeId = flowTypeId;
    }

    public String getFlowTypeName() {

        return flowTypeName;
    }

    public void setFlowTypeName(String flowTypeName) {

        this.flowTypeName = flowTypeName;
    }

    public Integer getManualSelectHandler() {

        return manualSelectHandler;
    }

    public void setManualSelectHandler(Integer manualSelectHandler) {

        this.manualSelectHandler = manualSelectHandler;
    }

    public Integer getPassAction() {

        return passAction;
    }

    public void setPassAction(Integer passAction) {

        this.passAction = passAction;
    }

    public Integer getReturnAction() {

        return returnAction;
    }

    public void setReturnAction(Integer returnAction) {

        this.returnAction = returnAction;
    }

    public Integer getRejectAction() {

        return rejectAction;
    }

    public void setRejectAction(Integer rejectAction) {

        this.rejectAction = rejectAction;
    }

    public Integer getDiscardAction() {

        return discardAction;
    }

    public void setDiscardAction(Integer discardAction) {

        this.discardAction = discardAction;
    }

    public Integer getCompleteAction() {

        return completeAction;
    }

    public void setCompleteAction(Integer completeAction) {

        this.completeAction = completeAction;
    }

    public String getActivity() {

        return activity;
    }

    public void setActivity(String activity) {

        this.activity = activity;
    }

    public String getCreatorName() {

        return creatorName;
    }

    public void setCreatorName(String creatorName) {

        this.creatorName = creatorName;
    }

    public String getOrganizationName() {

        return organizationName;
    }

    public void setOrganizationName(String organizationName) {

        this.organizationName = organizationName;
    }

    public Date getTaskArriveTime() {

        return taskArriveTime;
    }

    public void setTaskArriveTime(Date taskArriveTime) {

        this.taskArriveTime = taskArriveTime;
    }

    public Integer getTaskSourceType() {

        return taskSourceType;
    }

    public void setTaskSourceType(Integer taskSourceType) {

        this.taskSourceType = taskSourceType;
    }

    public Integer getTaskExpiration() {

        return taskExpiration;
    }

    public void setTaskExpiration(Integer taskExpiration) {

        this.taskExpiration = taskExpiration;
    }

    public Integer getTaskTimeout() {

        return taskTimeout;
    }

    public void setTaskTimeout(Integer taskTimeout) {

        this.taskTimeout = taskTimeout;
    }

    public Integer getTaskLocked() {

        return taskLocked;
    }

    public void setTaskLocked(Integer taskLocked) {

        this.taskLocked = taskLocked;
    }
}