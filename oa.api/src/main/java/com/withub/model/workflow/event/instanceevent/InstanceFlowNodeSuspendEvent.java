package com.withub.model.workflow.event.instanceevent;

public class InstanceFlowNodeSuspendEvent extends BaseInstanceEvent {

    private static final long serialVersionUID = 1L;

    public InstanceFlowNodeSuspendEvent(Object source, InstanceEventArgs args) {

        super(source, args);
    }
}
