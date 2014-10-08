package com.withub.model.workflow.po;

import com.withub.model.entity.AbstractEntity;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "WF_TASKCONTEXT")
public class TaskContext extends AbstractEntity {

    //=================== 属性声明============================================

    @ManyToOne(targetEntity = Task.class)
    @JoinColumn(name = "TaskId")
    private Task task;

    private String contextKey;

    private String contextValue;

    //=================== 属性方法 ============================================

    public Task getTask() {

        return task;
    }

    public void setTask(Task task) {

        this.task = task;
    }

    public String getContextKey() {

        return contextKey;
    }

    public void setContextKey(String contextKey) {

        this.contextKey = contextKey;
    }

    public String getContextValue() {

        return contextValue;
    }

    public void setContextValue(String contextValue) {

        this.contextValue = contextValue;
    }
}
