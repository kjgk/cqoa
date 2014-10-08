package com.withub.model.workflow.event.taskevent;


import com.withub.spring.SpringContextUtil;

public final class TaskEventPublisher {

    public static void publishTaskAbortEvent(Object source, TaskEventArgs args) {

        TaskAbortEvent event = new TaskAbortEvent(source, args);
        SpringContextUtil.getInstance().getApplicationContext().publishEvent(event);
    }

    public static void publishTaskCreateEvent(Object source, TaskEventArgs args) {

        TaskCreateEvent event = new TaskCreateEvent(source, args);
        SpringContextUtil.getInstance().getApplicationContext().publishEvent(event);
    }

    public static void publishModifyTaskCreateEvent(Object source, TaskEventArgs args) {

        ModifyTaskCreateEvent event = new ModifyTaskCreateEvent(source, args);
        SpringContextUtil.getInstance().getApplicationContext().publishEvent(event);
    }

    public static void publishTaskDeleteEvent(Object source, TaskEventArgs args) {

        TaskDeleteEvent event = new TaskDeleteEvent(source, args);
        SpringContextUtil.getInstance().getApplicationContext().publishEvent(event);
    }

    public static void publishTaskFinishEvent(Object source, TaskEventArgs args) {

        TaskFinishEvent event = new TaskFinishEvent(source, args);
        SpringContextUtil.getInstance().getApplicationContext().publishEvent(event);
    }

    public static void publishTaskRollbackEvent(Object source, TaskEventArgs args) {

        TaskRollbackEvent event = new TaskRollbackEvent(source, args);
        SpringContextUtil.getInstance().getApplicationContext().publishEvent(event);
    }

    public static void publishTaskSetAgentEvent(Object source, TaskEventArgs args) {

        TaskAgentSetEvent event = new TaskAgentSetEvent(source, args);
        SpringContextUtil.getInstance().getApplicationContext().publishEvent(event);
    }

    public static void publishTaskTakebackEvent(Object source, TaskEventArgs args) {

        TaskTakebackEvent event = new TaskTakebackEvent(source, args);
        SpringContextUtil.getInstance().getApplicationContext().publishEvent(event);
    }

    public static void publishTaskTransmitEvent(Object source, TaskEventArgs args) {

        TaskTransmitEvent event = new TaskTransmitEvent(source, args);
        SpringContextUtil.getInstance().getApplicationContext().publishEvent(event);
    }

    public static void publishTaskTimeoutEvent(Object source, TaskEventArgs args) {

        TaskTimeoutEvent event = new TaskTimeoutEvent(source, args);
        SpringContextUtil.getInstance().getApplicationContext().publishEvent(event);
    }

    public static void publishTaskWillTimeoutEvent(Object source, TaskEventArgs args) {

        TaskWillTimeoutEvent event = new TaskWillTimeoutEvent(source, args);
        SpringContextUtil.getInstance().getApplicationContext().publishEvent(event);
    }
}
