package com.withub.model.workflow.event.taskevent;

public class TaskWillTimeoutEvent extends BaseTaskEvent {

    private static final long serialVersionUID = 1L;

    public TaskWillTimeoutEvent(Object source, TaskEventArgs args) {

        super(source, args);

    }
}
