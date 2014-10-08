package com.withub.model.workflow.event.instanceevent;

public class InstanceApprovePassEvent extends BaseInstanceEvent{

	private static final long serialVersionUID = 1L;
	
	public InstanceApprovePassEvent(Object source,InstanceEventArgs args) {
		super(source,args);
	}
}
