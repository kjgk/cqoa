package com.withub.model.workflow.event.taskevent;

public class TaskAbortEvent extends BaseTaskEvent {

    private static final long serialVersionUID = 1L;

    public TaskAbortEvent(Object source, TaskEventArgs args) {
        super(source, args);

    }
}
