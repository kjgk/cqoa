package com.withub.model.workflow.event.taskevent;

public class TaskDeleteEvent extends BaseTaskEvent {

    public TaskDeleteEvent(Object source, TaskEventArgs args) {
        super(source, args);
    }
}
