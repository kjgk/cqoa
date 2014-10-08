package com.withub.model.workflow.event.instanceevent;

public class InstanceAbortEvent extends BaseInstanceEvent {

    private static final long serialVersionUID = 1L;

    public InstanceAbortEvent(Object source, InstanceEventArgs args) {
        super(source, args);
    }
}
