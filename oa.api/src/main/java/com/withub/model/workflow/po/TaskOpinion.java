package com.withub.model.workflow.po;

import com.withub.model.entity.AbstractEntity;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "WF_TASKOPINION")
public class TaskOpinion extends AbstractEntity {

    //=================== 属性声明============================================

    @ManyToOne(targetEntity = Task.class)
    @JoinColumn(name = "TaskId")
    private Task task;

    private String opinion;

    //=================== 属性方法 ============================================

    public Task getTask() {

        return task;
    }

    public void setTask(Task task) {

        this.task = task;
    }

    public String getOpinion() {

        return opinion;
    }

    public void setOpinion(String opinion) {

        this.opinion = opinion;
    }
}
