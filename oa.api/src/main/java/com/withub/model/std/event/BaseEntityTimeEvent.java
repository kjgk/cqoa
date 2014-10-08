package com.withub.model.std.event;

import org.springframework.context.ApplicationEvent;

public class BaseEntityTimeEvent extends ApplicationEvent {

    private EntityTimeEventArgs entityTimeEventArgs;

    public BaseEntityTimeEvent(Object source, EntityTimeEventArgs args) {

        super(source);
        entityTimeEventArgs = args;
    }

    public EntityTimeEventArgs getEntityTimeEventArgs() {

        return entityTimeEventArgs;
    }

    public void setEntityTimeEventArgs(EntityTimeEventArgs entityTimeEventArgs) {

        this.entityTimeEventArgs = entityTimeEventArgs;
    }

}