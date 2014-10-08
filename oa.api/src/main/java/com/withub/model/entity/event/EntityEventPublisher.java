package com.withub.model.entity.event;

import com.withub.model.entity.AbstractEntity;
import com.withub.spring.SpringContextUtil;

/**
 * 定义实体事件发布器
 */
public final class EntityEventPublisher {

    public static void publishEntityAddEvent(Object source, AbstractEntity entityInstance) {

        EntityAddEvent event = new EntityAddEvent(source, entityInstance);
        SpringContextUtil.getInstance().getApplicationContext().publishEvent(event);
    }

    public static void publishEntityUpdateEvent(Object source, AbstractEntity oldEntityInstance, AbstractEntity entityInstance) {

        EntityUpdateEvent event = new EntityUpdateEvent(source, oldEntityInstance, entityInstance);
        SpringContextUtil.getInstance().getApplicationContext().publishEvent(event);
    }

    public static void publishEntityLogicDeleteEvent(Object source, AbstractEntity entityInstance) {

        EntityLogicDeleteEvent event = new EntityLogicDeleteEvent(source, entityInstance);
        SpringContextUtil.getInstance().getApplicationContext().publishEvent(event);
    }

    public static void publishEntityDeleteEvent(Object source, AbstractEntity entityInstance) {

        EntityDeleteEvent event = new EntityDeleteEvent(source, entityInstance);
        SpringContextUtil.getInstance().getApplicationContext().publishEvent(event);
    }
}