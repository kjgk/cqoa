package com.withub.model.entity.event;

import com.withub.model.entity.AbstractEntity;
import org.springframework.context.ApplicationEvent;

public class EntityUpdateEvent extends ApplicationEvent {

    private AbstractEntity oldEntityInstance;

    private AbstractEntity entityInstance;


    public EntityUpdateEvent(Object source, AbstractEntity oldEntityInstance, AbstractEntity entityInstance) {

        super(source);
        this.oldEntityInstance = oldEntityInstance;
        this.entityInstance = entityInstance;
    }

    public AbstractEntity getOldEntityInstance() {

        return oldEntityInstance;
    }

    public void setOldEntityInstance(AbstractEntity oldEntityInstance) {

        this.oldEntityInstance = oldEntityInstance;
    }

    public AbstractEntity getEntityInstance() {

        return entityInstance;
    }

    public void setEntityInstance(AbstractEntity entityInstance) {

        this.entityInstance = entityInstance;
    }
}
