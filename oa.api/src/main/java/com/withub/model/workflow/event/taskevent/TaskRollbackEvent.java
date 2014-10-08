package com.withub.model.workflow.event.taskevent;

public class TaskRollbackEvent extends BaseTaskEvent {

    private static final long serialVersionUID = 1L;

    public TaskRollbackEvent(Object source, TaskEventArgs args) {
        super(source, args);

    }
}
