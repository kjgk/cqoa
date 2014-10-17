package com.withub.model.workflow.vo;

import com.withub.model.entity.AbstractEntity;

import java.util.List;

public class ApproveInfo extends AbstractEntity {

    private String taskId;

    private String opinion;

    private List<String> approvers;


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
}