package com.withub.model.workflow.event.taskevent;

import org.springframework.context.ApplicationEvent;

public class BaseTaskEvent extends ApplicationEvent {

    private TaskEventArgs taskEventArgs;

    public BaseTaskEvent(Object source, TaskEventArgs args) {
        super(source);
        taskEventArgs = args;
    }

    public TaskEventArgs getTaskEventArgs() {
        return taskEventArgs;
    }

    public void setTaskEventArgs(TaskEventArgs taskEventArgs) {
        this.taskEventArgs = taskEventArgs;
    }
}
