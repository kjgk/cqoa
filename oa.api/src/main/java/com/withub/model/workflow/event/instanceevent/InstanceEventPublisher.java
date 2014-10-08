package com.withub.model.workflow.event.instanceevent;


import com.withub.spring.SpringContextUtil;

public final class InstanceEventPublisher {

    public static void publishInstanceApprovePassEvent(Object source, InstanceEventArgs args) {

        InstanceApprovePassEvent event = new InstanceApprovePassEvent(source, args);
        SpringContextUtil.getInstance().getApplicationContext().publishEvent(event);
    }

    public static void publishInstanceApproveRejectEvent(Object source, InstanceEventArgs args) {

        InstanceApproveRejectEvent event = new InstanceApproveRejectEvent(source, args);
        SpringContextUtil.getInstance().getApplicationContext().publishEvent(event);
    }

    public static void publishInstanceFlowNodeVoteFinishEvent(Object source, InstanceEventArgs args) {

        InstanceFlowNodeVoteFinishEvent event = new InstanceFlowNodeVoteFinishEvent(source, args);
        SpringContextUtil.getInstance().getApplicationContext().publishEvent(event);
    }

    public static void publishInstanceCreateEvent(Object source, InstanceEventArgs args) {

        InstanceCreateEvent event = new InstanceCreateEvent(source, args);
        SpringContextUtil.getInstance().getApplicationContext().publishEvent(event);
    }

    public static void publishInstanceAbortEvent(Object source, InstanceEventArgs args) {

        InstanceAbortEvent event = new InstanceAbortEvent(source, args);
        SpringContextUtil.getInstance().getApplicationContext().publishEvent(event);
    }

    public static void publishInstanceDeleteEvent(Object source, InstanceEventArgs args) {

        InstanceDeleteEvent event = new InstanceDeleteEvent(source, args);
        SpringContextUtil.getInstance().getApplicationContext().publishEvent(event);
    }

    public static void publishInstanceFinishEvent(Object source, InstanceEventArgs args) {

        InstanceFinishEvent event = new InstanceFinishEvent(source, args);
        SpringContextUtil.getInstance().getApplicationContext().publishEvent(event);
    }

    public static void publishInstanceApproveReturnEvent(Object source, InstanceEventArgs args) {

        InstanceApproveReturnEvent event = new InstanceApproveReturnEvent(source, args);
        SpringContextUtil.getInstance().getApplicationContext().publishEvent(event);
    }

    public static void publishInstanceRollBackToSubmitNodeEvent(Object source, InstanceEventArgs args) {

        InstanceRollBackToSubmitNodeEvent event = new InstanceRollBackToSubmitNodeEvent(source, args);
        SpringContextUtil.getInstance().getApplicationContext().publishEvent(event);
    }

    public static void publishInstanceFlowNodeSuspendEvent(Object source, InstanceEventArgs args) {

        InstanceFlowNodeSuspendEvent event = new InstanceFlowNodeSuspendEvent(source, args);
        SpringContextUtil.getInstance().getApplicationContext().publishEvent(event);
    }
}

