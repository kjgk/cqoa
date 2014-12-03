package com.withub.model.workflow.vo;

import com.withub.model.entity.AbstractEntity;
import com.withub.model.workflow.po.TaskContext;

import java.util.List;

public class ApproveInfo extends AbstractEntity {

    private String taskId;

    private String opinion;

    private List<String> approvers;

    private List<TaskContext> contextList;


    //=================== 属性方法 ============================================


    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getOpinion() {
        return opinion;
    }

    public void setOpinion(String opinion) {
        this.opinion = opinion;
    }

    public List<String> getApprovers() {
        return approvers;
    }

    public void setApprovers(List<String> approvers) {
        this.approvers = approvers;
    }

    public List<TaskContext> getContextList() {
        return contextList;
    }

    public void setContextList(List<TaskContext> contextList) {
        this.contextList = contextList;
    }
}