package com.withub.model.workflow.event.taskevent;

import com.withub.model.workflow.po.Task;

public class TaskEventArgs {

    private Task task;

    public Task getTask() {

        return task;
    }

    public void setTask(Task task) {

        this.task = task;
    }

}
