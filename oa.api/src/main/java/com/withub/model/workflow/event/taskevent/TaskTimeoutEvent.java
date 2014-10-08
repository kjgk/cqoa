package com.withub.model.workflow.event.taskevent;

public class TaskTimeoutEvent extends BaseTaskEvent {

    private static final long serialVersionUID = 1L;

    public TaskTimeoutEvent(Object source, TaskEventArgs args) {

        super(source, args);

    }
}
