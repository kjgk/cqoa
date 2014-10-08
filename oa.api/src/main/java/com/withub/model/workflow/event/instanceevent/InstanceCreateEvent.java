package com.withub.model.workflow.event.instanceevent;

public class InstanceCreateEvent extends BaseInstanceEvent {

    private static final long serialVersionUID = 1L;

    public InstanceCreateEvent(Object source, InstanceEventArgs args) {
        super(source, args);
    }
}
