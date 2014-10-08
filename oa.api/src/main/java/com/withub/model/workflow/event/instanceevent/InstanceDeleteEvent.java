package com.withub.model.workflow.event.instanceevent;

public class InstanceDeleteEvent extends BaseInstanceEvent {

    private static final long serialVersionUID = 1L;

    public InstanceDeleteEvent(Object source, InstanceEventArgs args) {
        super(source, args);
    }
}
