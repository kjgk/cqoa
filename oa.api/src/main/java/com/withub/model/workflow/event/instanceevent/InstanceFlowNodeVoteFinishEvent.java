package com.withub.model.workflow.event.instanceevent;

public class InstanceFlowNodeVoteFinishEvent extends BaseInstanceEvent {

    private static final long serialVersionUID = 1L;

    public InstanceFlowNodeVoteFinishEvent(Object source, InstanceEventArgs args) {
        super(source, args);
    }
}
