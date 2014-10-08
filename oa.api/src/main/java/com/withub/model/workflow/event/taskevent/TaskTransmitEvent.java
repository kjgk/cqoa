package com.withub.model.workflow.event.taskevent;

public class TaskTransmitEvent extends BaseTaskEvent {

    private static final long serialVersionUID = 1L;

    public TaskTransmitEvent(Object source, TaskEventArgs args) {
        super(source, args);

    }
}
