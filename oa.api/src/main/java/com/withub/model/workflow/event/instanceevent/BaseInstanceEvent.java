package com.withub.model.workflow.event.instanceevent;

import org.springframework.context.ApplicationEvent;

public class BaseInstanceEvent extends ApplicationEvent {

    private static final long serialVersionUID = 1L;

    private InstanceEventArgs instanceEventArgs;

    public BaseInstanceEvent(Object source, InstanceEventArgs args) {
        super(source);
        instanceEventArgs = args;
    }

    public InstanceEventArgs getInstanceEventArgs() {
        return instanceEventArgs;
    }

    public void setInstanceEventArgs(InstanceEventArgs instanceEventArgs) {
        this.instanceEventArgs = instanceEventArgs;
    }

}
