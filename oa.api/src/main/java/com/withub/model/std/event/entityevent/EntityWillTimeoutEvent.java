package com.withub.model.std.event.entityevent;

import com.withub.model.entity.AbstractEntity;
import org.springframework.context.ApplicationEvent;

public class EntityWillTimeoutEvent extends ApplicationEvent {

    private AbstractEntity entityInstance;

    public EntityWillTimeoutEvent(Object source, AbstractEntity entityInstance) {

        super(source);
        this.entityInstance = entityInstance;
    }

    public AbstractEntity getEntityInstance() {

        return entityInstance;
    }

    public void setEntityInstance(AbstractEntity entityInstance) {

        this.entityInstance = entityInstance;
    }
}
