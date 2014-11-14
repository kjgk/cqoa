package com.withub.model.workflow.vo;


import com.withub.model.system.po.User;

import java.io.Serializable;
import java.util.List;

public class TaskFlowNodeInfo implements Serializable {

    private String flowNodeName;

    private Integer passAction;

    private Integer returnAction;

    private Integer rejectAction;

    private Integer completeAction;

    private Integer discardAction;

    private Integer manualSelectHandler;

    private Integer handlerFetchCount;

    private String nextFlowNodeName;

    private List<User> handlerList;

    public String getFlowNodeName() {
        return flowNodeName;
    }

    public void setFlowNodeName(String flowNodeName) {
        this.flowNodeName = flowNodeName;
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

    public Integer getCompleteAction() {
        return completeAction;
    }

    public void setCompleteAction(Integer completeAction) {
        this.completeAction = completeAction;
    }

    public Integer getDiscardAction() {
        return discardAction;
    }

    public void setDiscardAction(Integer discardAction) {
        this.discardAction = discardAction;
    }

    public Integer getManualSelectHandler() {
        return manualSelectHandler;
    }

    public void setManualSelectHandler(Integer manualSelectHandler) {
        this.manualSelectHandler = manualSelectHandler;
    }

    public Integer getHandlerFetchCount() {
        return handlerFetchCount;
    }

    public void setHandlerFetchCount(Integer handlerFetchCount) {
        this.handlerFetchCount = handlerFetchCount;
    }

    public String getNextFlowNodeName() {
        return nextFlowNodeName;
    }

    public void setNextFlowNodeName(String nextFlowNodeName) {
        this.nextFlowNodeName = nextFlowNodeName;
    }

    public List<User> getHandlerList() {
        return handlerList;
    }

    public void setHandlerList(List<User> handlerList) {
        this.handlerList = handlerList;
    }
}
