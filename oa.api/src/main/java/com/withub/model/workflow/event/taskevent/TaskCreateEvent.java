package com.withub.model.workflow.event.taskevent;

public class TaskCreateEvent extends BaseTaskEvent {
    
    public TaskCreateEvent(Object source, TaskEventArgs args) {
        super(source, args);
    }
}
