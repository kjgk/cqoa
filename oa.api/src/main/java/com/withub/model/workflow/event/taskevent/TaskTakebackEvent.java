package com.withub.model.workflow.event.taskevent;

public class TaskTakebackEvent extends BaseTaskEvent {

    private static final long serialVersionUID = 1L;

    public TaskTakebackEvent(Object source, TaskEventArgs args) {
        super(source, args);

    }
}
