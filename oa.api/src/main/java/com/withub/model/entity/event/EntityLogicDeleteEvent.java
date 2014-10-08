package com.withub.model.entity.event;

import com.withub.model.entity.AbstractEntity;
import org.springframework.context.ApplicationEvent;

public class EntityLogicDeleteEvent extends ApplicationEvent {

    private AbstractEntity entityInstance;

    public EntityLogicDeleteEvent(Object source, AbstractEntity entityInstance) {

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
