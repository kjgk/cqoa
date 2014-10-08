package com.withub.model.workflow.event.taskevent;

public class TaskFinishEvent extends BaseTaskEvent {

    private static final long serialVersionUID = 1L;

    public TaskFinishEvent(Object source, TaskEventArgs args) {
        super(source, args);

    }
}
