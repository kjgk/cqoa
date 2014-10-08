package com.withub.model.std.event.entityevent;

import com.withub.model.entity.AbstractEntity;
import org.springframework.context.ApplicationEvent;

public class EntityTimeoutEvent extends ApplicationEvent {

    private AbstractEntity entityInstance;

    public EntityTimeoutEvent(Object source, AbstractEntity entityInstance) {

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
