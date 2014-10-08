package com.withub.model.workflow.event.instanceevent;

public class InstanceApproveRejectEvent extends BaseInstanceEvent {

    private static final long serialVersionUID = 1L;

    public InstanceApproveRejectEvent(Object source, InstanceEventArgs args) {
        super(source, args);
    }
}
