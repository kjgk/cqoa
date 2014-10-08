package com.withub.model.std.event.entityevent;

import com.withub.model.entity.AbstractEntity;
import com.withub.spring.SpringContextUtil;

public final class EntityInstanceEventPublisher {

    public static void publishEntityTimeoutEvent(Object source, AbstractEntity entityInstance) {

        EntityTimeoutEvent event = new EntityTimeoutEvent(source, entityInstance);
        SpringContextUtil.getInstance().getApplicationContext().publishEvent(event);
    }

    public static void publishEntityWillTimeoutEvent(Object source, AbstractEntity entityInstance) {

        EntityWillTimeoutEvent event = new EntityWillTimeoutEvent(source, entityInstance);
        SpringContextUtil.getInstance().getApplicationContext().publishEvent(event);
    }
}