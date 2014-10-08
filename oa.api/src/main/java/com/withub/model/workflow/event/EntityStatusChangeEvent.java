package com.withub.model.workflow.event;

import com.withub.model.entity.AbstractBaseEntity;
import org.springframework.context.ApplicationEvent;

public class EntityStatusChangeEvent extends ApplicationEvent {

    private static final long serialVersionUID = 1L;

    private AbstractBaseEntity entityInstance;

    public EntityStatusChangeEvent(Object source, AbstractBaseEntity entityInstance) {

        super(source);
        this.entityInstance = entityInstance;
    }

    public AbstractBaseEntity getEntityInstance() {

        return entityInstance;
    }

    public void setEntityInstance(AbstractBaseEntity entityInstance) {

        this.entityInstance = entityInstance;
    }
}
