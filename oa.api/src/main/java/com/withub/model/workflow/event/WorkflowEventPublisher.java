package com.withub.model.workflow.event;

import com.withub.model.entity.AbstractBaseEntity;
import com.withub.spring.SpringContextUtil;

public final class WorkflowEventPublisher {

    public static void publishDecisionCreateEvent(Object source, DecisionEventArgs args) {

        DecisionCreateEvent event = new DecisionCreateEvent(source, args);
        SpringContextUtil.getInstance().getApplicationContext().publishEvent(event);
    }

    public static void publishEntityStatusChangeEvent(Object source, AbstractBaseEntity entityInstance) {

        EntityStatusChangeEvent event = new EntityStatusChangeEvent(source, entityInstance);
        SpringContextUtil.getInstance().getApplicationContext().publishEvent(event);
    }
}
