package com.withub.model.workflow.event.instanceevent;

public class InstanceFinishEvent extends BaseInstanceEvent {

    private static final long serialVersionUID = 1L;

    public InstanceFinishEvent(Object source, InstanceEventArgs args) {
        super(source, args);
    }
}
